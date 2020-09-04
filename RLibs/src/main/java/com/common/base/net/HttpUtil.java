package com.common.base.net;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.common.base.bean.PubData;
import com.common.base.bean.PubDataList;
import com.common.base.utils.LogUtil;
import com.common.base.utils.base.ConfusionStrUtil;
import com.common.base.utils.DateStr;
import com.common.base.utils.base.JsonTool;
import com.common.base.utils.base.XmlUtil;
import com.common.base.utils.key.AESUtil;
import com.common.base.utils.key.Keys;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HSK° on 2018/9/10.
 * --function:  调用网络封装的工具类
 */
@SuppressWarnings("unchecked")
public class HttpUtil {

    private String TAG = getClass().getSimpleName();

    private OkHttpClient mOkHttpClient;
    private final ExecutorService executorService;   //线程池
    private volatile static HttpUtil mInstance;
    private static Context mContext;
    private static String baseURL;
    private Handler handler;

//    private UserInfo userInfo;//用户信息表

    private Tag tag;  //标记，用来区分是哪个请求。
    private int nums = 0;//失败次数

    private HttpUtil(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        executorService = Executors.newCachedThreadPool();
        handler = new Handler();
//        userInfo = DataSupport.findFirst(UserInfo.class);

    }

    /**
     * 在application时候调用 (初始化)
     *
     * @param okHttpClient okhttpclient
     * @param baseUrl      基础url
     */
    public static HttpUtil initClient(OkHttpClient okHttpClient, Context context, String baseUrl) {

        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil(okHttpClient);
                }
            }
        }
        if (context != null) {
            mContext = context;
        }

        if (!TextUtils.isEmpty(baseUrl)) {
            baseURL = baseUrl;
        }

        if (mContext == null || TextUtils.isEmpty(baseURL))
            throw new IllegalArgumentException("HttpUtil 参数初始化错误...");
        return mInstance;
    }

    public static HttpUtil getInstance() {
        return initClient(null, null, null);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 存储过程调用此方法  proc
     *
     * @param req      Map
     * @param what     和handler.what 作用一致，用来区分
     * @param callBack 接口回调
     */

    public void loadData(final Map req, final int what, final HttpResponseCallBack callBack) {
        tag = new Tag(what);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final PubData pubData = loadData(req);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null && pubData != null) {
                            callBack.onResponse(pubData, what);
                        }
                    }
                });

            }
        });

    }


    /**
     * sql调用此方法
     *
     * @param req      Map
     * @param what     和handler.what 作用一致，用来区分
     * @param callBack 接口回调
     */

    public void loadDataList(final Map req, final int what, final HttpResponseCallBack callBack) {
        tag = new Tag(what);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final PubDataList pubDataList = loadDataList(req);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null && pubDataList != null) {
                            callBack.onResponse(pubDataList, what);
                        }
                    }
                });

            }
        });

    }

    /**
     * // updateData 写sql只传会是否插入成功 不会传回要查询的数据
     * // 若写存储过程 则可以返回要的的数据 但 此接口不返回list
     */
    public void updateData(final Map reqxml, final int what, final HttpResponseCallBack callBack) {
        tag = new Tag(what);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                reqxml.put("OPER.NAME", 2);
                PubData post = post(reqxml);
                if (callBack != null && post != null) {
                    callBack.onResponse(post, what);
                }
            }
        });
    }


    public void updateData(final String reqxml, final HttpResponseCallBack callBack) {
        final Map<String, Object> req = JsonTool.getMap4Json(reqxml);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                req.put("OPER.NAME", 2);
                final PubData post = post(XmlUtil.json2map(reqxml));
                if (callBack != null && post != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onResponse(post, 0);
                        }
                    });
                }
            }
        });

    }


    /**
     * 以下都是业务层代码 (可以不必关注）
     */


    private String doPost(final String content) throws IOException {

        RequestBody requestBody = new FormBody.Builder()
                .add("str", content).build();

        Request request = new Request.Builder()
                .url(baseURL + "publicpost.do")
                .tag(tag.getTag())
                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .post(requestBody)
                .build();

        Response response;
        String responseData = null;
        response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            responseData = response.body().string();
        }
        return responseData;
    }

    private PubDataList loadDataList(Map map) {
        return loadDataList(XmlUtil.obj2JSON(map));
    }

    @SuppressWarnings("unchecked")
    private PubDataList loadDataList(String reqxml) {
        LogUtil.d(TAG, "请求reqxml" + reqxml);
        reqxml = addSessionId(reqxml);
//        KLog.debug(TAG, "请求的报文sessionReqxml" + reqxml);
        Map<String, Object> req = JsonTool.getMap4Json(reqxml);
        req.put("OPER.NAME", "1");
        PubData pd = post(req);
        PubDataList pdl = new PubDataList();
        if (pd != null) {
            pdl.setCode(pd.getCode());
            pdl.setPage(pd.getPage());
            if (pd.getData() != null && pd.getData().containsKey("list")) {
                pdl.setData((List<Map<String, Object>>) (pd.getData().get("list")));
            }
        } else {
            pdl.setCode("01");
        }
        pd = null;
        return pdl;
    }

    private PubData loadData(Map map) {
        return loadData(XmlUtil.obj2JSON(map));
    }

    private PubData loadData(String reqxml) {
        LogUtil.d(TAG, "请求的reqxml" + reqxml);
        reqxml = addSessionId(reqxml);
//        Log.d(TAG, "请求的报文sessionReqxml=" + reqxml);
        Map<String, Object> req = JsonTool.getMap4Json(reqxml);
        req.put("OPER.NAME", 0);
        return post(req);
    }

    public String updateInfoAttachment_MD5(String reqxml) {
        Map<String, Object> req = JsonTool.getMap4Json(reqxml);
        req.put("OPER.NAME", "9");
        req.put("start", Double.valueOf(String.valueOf(req.get("start"))).intValue() + "");
        PubData pd = post(req);
        return pd.getCode();
    }

    /**
     * @param xml 。
     * @return 增加sessionId
     */
    private String addSessionId(String xml) {
        String resXml = "{\"sessionId\":\"" + "\"";
        if (xml != null && xml.length() > 1) {
            resXml = resXml + "," + xml.substring(1);
        } else {
            resXml = resXml + "}";
        }
        return resXml;
    }

    private PubData post(Map<String, Object> reqxml) {
        PubData template = null;
        String req = null;
        try {
            Object vedio = null;
            if ("9".equals(reqxml.get("OPER.NAME"))) {// 附件上报需单独处理
                vedio = reqxml.get("VEDIO");
                reqxml.remove("VEDIO");
            }
            String key = Keys.getTKey(mContext);
            LogUtil.d(TAG, "key值" + key);

            if (TextUtils.isEmpty(key)) {// 为空，则重新取号,调用获取会话ID和临时密钥并返回
                String r = relogin();
                if ("90".equals(r)) {// 取号异常
                    template = new PubData();
                    template.setCode("90");
                    template.setMsg("客户端异常!");
                    return template;
                } else if ("0".equals(r)) {
                    key = Keys.getTKey(mContext);
                } else {
                    template = new PubData();// 服务端提示信息，直接返回
                    template.setCode("98");
                    template.setMsg(r);
                    return template;
                }
            }
            req = AESUtil.encrypt(JsonTool.obj2JSON(reqxml), key);// 内容加密
            req = ConfusionStrUtil.encryptionStrs(
                    Keys.getSid(mContext), req);// 混淆
            if (vedio != null) {// 附件上报特殊处理
                Map<String, Object> tmap = new HashMap<>();
                tmap.put("A", req);
                tmap.put("B", vedio);
                req = JsonTool.obj2JSON(tmap);
            }
            String xml = doPost(req);
            if (xml != null) {
                try {
                    xml = AESUtil.decrypt(xml, Keys.getTKey(mContext));
                    if ((xml.startsWith("{") && xml.endsWith("}")) || (xml.startsWith("[") && xml.endsWith("]"))) {
                        nums = 0;
                    } else {
                        Keys.setTKey("", mContext);
                        post(reqxml);
                    }

                } catch (BadPaddingException e) {// 解密时发现密钥不正确，使用原始密钥解开内容。异常情况
                    nums++;
                    LogUtil.e(TAG, "次数:" + nums);

                    if (nums <= 3) {
                        Keys.setTKey("", mContext);
                        template = post(reqxml);
                    }
                    return template;
                } catch (IllegalArgumentException e) {
                    xml = AESUtil.decrypt(xml, Keys.getDKey(mContext));
                }

                LogUtil.d(TAG, "返回xml=" + xml);
                xml = xml.substring(1, xml.length() - 1);
                PubData planInfo = null;
                if ("1".equals(reqxml.get("OPER.NAME"))) {
                    PubDataList plist = (PubDataList) JsonTool.getObject4JsonString(xml, PubDataList.class);
                    planInfo = new PubData();
                    if (plist != null) {
                        planInfo.setCode(plist.getCode());
                        planInfo.setPage(plist.getPage());
                        planInfo.setMsg(plist.getMsg());
                        Map<String, Object> m = new HashMap<>();
                        m.put("list", plist.getData());
                        planInfo.setData(m);
                    }
                } else {
                    planInfo = (PubData) JsonTool.getObject4JsonString(xml,
                            PubData.class);
                }
                template = planInfo;
            }
        }catch (InvalidKeyException e){
            nums++;
            if (nums <= 3) {
                Keys.setTKey("",mContext);
                template = post(reqxml);
            }
            e.printStackTrace();
        }

        catch (IOException e1) {   // todo: 这是无网络进入此异常，后面可以添加一些之定义状态码
            template = null;
            e1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            template = null;
        }
        return template;
    }

    /**
     * @return * 重新获取身份信息。
     */
    private String relogin() {
        String req = null;
        try {
            String content = DateStr.yymmddStr();
            String key1 = Keys.getDKey(mContext);
            req = AESUtil.encrypt(content, key1);
            String xml = doPost(req);
            if (xml != null) {
                LogUtil.d(TAG, "relogin:" + xml);
                xml = AESUtil.decrypt(xml, Keys.getDKey(mContext));
                if (xml.startsWith("{") && xml.endsWith("}")) {
                    req = String.valueOf(JsonTool.getMap4Json(xml).get("content"));
                } else {
                    List<String> l = ConfusionStrUtil.decryptionStr(xml);
                    Keys.setSid(l.get(0), mContext);
                    String key = l.get(1);
                    Keys.setTKey(key + DateStr.yymmddStr().substring(5), mContext);
                    req = "0";
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            req = "90";
        } catch (Exception ex) {
            ex.printStackTrace();
            req = "90";
        }
        return req;
    }

    class Tag {

        private int tag;

        Tag(int tag) {
            this.tag = tag;
        }

        private int getTag() {
            return tag;
        }
    }


}