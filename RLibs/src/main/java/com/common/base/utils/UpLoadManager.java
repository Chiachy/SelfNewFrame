package com.common.base.utils;

/**
 * Created by HSK° on 2018/9/10.
 * --function:
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.common.base.bean.Picture;
import com.common.base.net.HttpUtil;
import com.common.base.ui.widget.CircleProgressView;
import com.common.base.utils.base.FileAccessI;
import com.common.base.utils.base.FileUtil;
import com.common.base.utils.base.JsonTool;
import com.common.base.utils.base.MD5;
import com.common.base.utils.watermask.WaterMask;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.util.Base64.NO_WRAP;

/**
 * Created by HSK° on 2017/12/27.
 * 上传文件工具类
 * 可上传 图片音频 视频....
 * 自动识别照片 压缩并添加水印
 */

public class UpLoadManager {

    private static final int UPLOAD_CODE = 666;
    private List<Picture> message;
    private int failueNum = 0, Num = 0;
    private String projectId;
    private ArrayList<String> url;
    private ArrayList<String> zipUrlList;     //压缩的文件路径
    private ArrayList<String> noZipUrlList;  //未压缩的文件路径
    private String location = "";                //地理位置
    private int indexSize = 0;
    private boolean needDateWaterMask = true;//是否需要日期水印,默认为true

    private Context context;
    private Handler handler;
    private String type = "";  //1签到 2申述

    private Toast mToast;
    private OnSuccessUpListener listener;
    private CircleProgressView circleImageView;


    private UpLoadManager() {
    }

    private static class Holder {
        private static UpLoadManager UpLoadManager = new UpLoadManager();
    }

    /**
     * 线程安全的单利模式
     */
    public static UpLoadManager getInstance() {
        return Holder.UpLoadManager;
    }

    /**
     * 初始化一些参数(外勤用这个 因为外勤要区分上报附件和审批附件区别开来)
     */
    public UpLoadManager init(@NonNull Context context, String type) {
        circleImageView = new CircleProgressView(context);
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.context = weakReference.get();
        this.type = type;
        this.message = new ArrayList<>();
        this.zipUrlList = new ArrayList<>();
        handler = new upLoadHandler();
        noZipUrlList = new ArrayList<>();
        return this;
    }

    /**
     * 初始化一些参数(其他项目用这个方法)
     */
    public UpLoadManager init(@NonNull Context context) {
        circleImageView = new CircleProgressView(context);
        this.context = context;
        this.message = new ArrayList<>();
        this.zipUrlList = new ArrayList<>();
        handler = new upLoadHandler();
        noZipUrlList = new ArrayList<>();
        return this;
    }


    /**
     * id 必须
     */
    public UpLoadManager setId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    /**
     * 附件路径 (必须)
     */
    public UpLoadManager url(ArrayList<String> url) {
        this.url = url;
        return this;
    }

    /**
     * 成功的回调，非必须
     */

    public UpLoadManager addSuccessUpListener(OnSuccessUpListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * @param location 添加地理位置水印
     *                 默认水印只有时间（非必须）
     */
    public UpLoadManager addLocationMark(String location) {
        this.location = location;
        return this;
    }

    /**
     * 是否需要 日期水印
     * 默认 为true 需要
     */
    public UpLoadManager isNeedDateWaterMask(boolean needDateWaterMask) {
        this.needDateWaterMask = needDateWaterMask;
        return this;
    }


    /**
     * 上传
     */
    public UpLoadManager upload() {
        if (handler != null && url != null && !TextUtils.isEmpty(projectId) && context != null) {
            circleImageView.setProgress(0, "0%");
            circleImageView.show();

            filterData(url);

            if (noZipUrlList.size() > 0) {
                for (int i = 0; i < noZipUrlList.size(); i++) {
                    String filepath = noZipUrlList.get(i);
                    String ext = filepath.substring(filepath.lastIndexOf(".") + 1);
                    saveToList(filepath, UUID.randomUUID().toString() + "." + ext, ext,
                            "0", filepath.substring(filepath.lastIndexOf("/") + 1));
                }
            }

            if (zipUrlList.size() > 0) {
                Luban.with(context)
                        .load(zipUrlList)                                   // 传人要压缩的图片列表
                        .ignoreBy(100)                                  // 忽略不压缩图片的大小
//                        .setTargetDir(Constants.CROUP_PHOTO_DIR)          // 设置压缩后文件存储位置
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) { //回调是一张一张图片回调的
                                //后缀
                                String filepath = file.getAbsolutePath();
                                String ext = filepath.substring(filepath.lastIndexOf(".") + 1);

                                if (needDateWaterMask)
                                    drawWasterMask(filepath);

                                saveToList(filepath, UUID.randomUUID().toString() + "." + ext, ext,
                                        "0", filepath.substring(filepath.lastIndexOf("/") + 1));
                                indexSize++;
//                                KLog.e("noZipUrlList---ppppp", noZipUrlList.size());
                                if (url.size() == indexSize + noZipUrlList.size()) {
                                    LogUtil.d("zip image begin upload", url.size() + "大小");
                                    cutFileUploaddetail(message.get(0).getExt(), message
                                            .get(0).getPicturePath(), message.get(0).getPicturename(), projectId, 0, message.get(0)
                                            .getStartPos(), message.get(0).getIslast(), message.get(0).getSize());
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
//                                KLog.e("压缩错误", e.getMessage());
                            }
                        }).launch();    //启动压缩
            } else {
                cutFileUploaddetail(message.get(0).getExt(), message
                        .get(0).getPicturePath(), message.get(0).getPicturename(), projectId, 0, message.get(0)
                        .getStartPos(), message.get(0).getIslast(), message.get(0).getSize());
            }


        } else {
            showToast("参数错误,图片上传失败!");
            throw new IllegalArgumentException("你必须初始化好参数，详见UpLoadManager的upload（）");
        }
        return this;

    }

    /**
     * 筛选出 图片文件进行压缩
     * 其他文件暂不考虑
     */
    private void filterData(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String ext = arrayList.get(i).substring(arrayList.get(i).lastIndexOf(".") + 1);
            String path = arrayList.get(i);
            if ("png".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)) {
                zipUrlList.add(path);
            } else {
                noZipUrlList.add(path);
            }
        }
    }


    /**
     * 绘制水印
     *
     * @param path 路径
     */
    private void drawWasterMask(String path) {
        WaterMask.WaterMaskParam waterMaskParam = new WaterMask.WaterMaskParam();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        waterMaskParam.txt.add(location);
        waterMaskParam.txt.add(formater.format(curDate));
        waterMaskParam.txtColor = Color.parseColor("#FFFFFF");
        waterMaskParam.location = WaterMask.DefWaterMaskParam.Location.left_top;
        WaterMask.draw(context, BitmapFactory.decodeFile(path), path, waterMaskParam);
    }


    /**
     * @param path 文件路径
     * @param name 名称
     */
    private void saveToList(String path, String name, String ext, String type, String time) {

        long[] array = cutFileUploadNum(path);
        if (array != null && array.length > 0) {
            int length = array.length;

            for (int i = 0; i < length; i++) {
                Picture pic = new Picture();
                if (i == (length - 1)) {

                    pic.setPicturePath(path);
                    pic.setState("0");
                    pic.setType(type);
                    pic.setExt(ext);
                    pic.setStartPos(array[i]);
                    pic.setIslast("1");
                    pic.setSize(time);
                    if ("3gp".equals(ext))
                        pic.setPicturename(name);
                    else
                        pic.setPicturename(name);
                } else {

                    pic.setPicturePath(path);
                    pic.setState("0");
                    pic.setType(type);
                    pic.setExt(ext);
                    pic.setStartPos(array[i]);
                    pic.setIslast("0");
                    pic.setSize(time);
                    if ("3gp".equals(ext))
                        pic.setPicturename(name);
                    else
                        pic.setPicturename(name);
                }
                message.add(pic);
            }
        }
    }


    private long[] cutFileUploadNum(String filepath) {
        long[] arrayStr = null;
        try {
            FileAccessI fileAccessI = new FileAccessI(filepath, 0);
            Long nStartPos = 0l;
            Long length = fileAccessI.getFileLength();
            int mBufferSize = 1024 * 100; // 每次处理1024 * 100字节 100KB
            int lens = (int) (length / mBufferSize) + 1;
            arrayStr = new long[lens];
            FileAccessI.Detail detail;
            long nRead = 0l;
            long nStart = nStartPos;
            int i = 0;

            while (nStart < length) {
                arrayStr[i] = nStart;
                detail = fileAccessI.getContent(nStart);
                nRead = detail.length;
                nStart += nRead;

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayStr;
    }


    private void cutFileUploaddetail(String fileType, String filePath,
                                     String vedioFileName, String id, int pos, long startpos,
                                     String islast, String times) {
        try {
            FileAccessI fileAccessI = new FileAccessI(filePath, 0);
            Long length = fileAccessI.getFileLength();
            FileAccessI.Detail detail;
            long nRead = 0l;
            long nStart = startpos;

            if (nStart < length) {
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String lastValidate = null;
                if ("0".equals(islast)) {
                    detail = fileAccessI.getContent(nStart);
                    nRead = detail.length;
                } else {
                    detail = fileAccessI.getContent_last(nStart);
                    nRead = detail.length;
                }

                Map<String, Object> resMap = new HashMap<String, Object>(); //
                resMap.put("FileName", vedioFileName);// UUID.randomUUID().toString()
                // +"."+fileType
                /**
                 * todo：这里使用的是java1.8的Base64，而之前的是apach中的Base64；如有问题
                 *  @param   已修正，第二个参数改为 NO_WRAP
                 */
                resMap.put("VEDIO", new String(Base64.encode(detail.b, NO_WRAP)));
                resMap.put("start", nStart);
                resMap.put("ext", fileType);
                if ((nStart + nRead) >= length) {
                    resMap.put("islast", "1");
                    System.out.println("1" + "  " + islast);
                    lastValidate = MD5.getFileMD5String(new File(filePath));
                    resMap.put("MD5Str", lastValidate);
                } else {
                    resMap.put("islast", "0");
                    System.out.println("0" + "  " + islast);
                    resMap.put("MD5Str", lastValidate);
                }
                resMap.put("TIMES", times);
                resMap.put("OPTTYPE", TextUtils.isEmpty(type) ? "A" : type);    //1 签到 2 申述 ,之前一直是A，规则是服务端定的
                String imagepath = "/images/attachment/" + year + "/" + month
                        + "/" + day;
                resMap.put("path", imagepath); // pub文件升级则
                resMap.put("UPLOADID", id);
                resMap.put("sqlType", "sql");
                resMap.put("sqlKey", "sql_upload_picorvideo_client");
                String xml = JsonTool.maptojson(resMap);
                upAttachment(xml, pos);
            }
        } catch (Exception e) {
            //todo:异常处理
//            ExceptionUtil expUtil = new ExceptionUtil(context);
//            expUtil.saveException(e);
        }

    }

    /**
     * 在子线程处理，发送到handler 更新UI
     */
    private void upAttachment(final String xml, final int pos) {
        new Thread() {
            public void run() {
                String item;
                item = HttpUtil.getInstance().updateInfoAttachment_MD5(xml);
                Message msg = handler.obtainMessage();
                msg.obj = item;
                msg.what = UPLOAD_CODE;
                msg.arg1 = pos;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 显示进度条百分比
     */
    private void showProgressDialog(int currentProgress, int total) {
        int percent = (currentProgress + 1) * 100 / total;
        circleImageView.setProgress(percent, percent + "%");
    }


    /**
     * 进度条消失
     */
    private void dissProgressDialog(int percent, String text) {
        if (circleImageView != null && circleImageView.isShowing()) {
            circleImageView.setProgress(percent, text);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    circleImageView.dissmiss();
                }
            }, 500);//500ms延迟为了让用户看到
        }
    }


    private class upLoadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case UPLOAD_CODE:
                    if (context == null) {
                        return;
                    }

                    String item = (String) msg.obj;
                    int pos = msg.arg1;
                    if (!"99".equals(item) && !"04".equals(item)) {
                        if (message != null && message.size() != 0) {
                            showProgressDialog(pos + 1, message.size());
                        }
                        failueNum = 0;
                        if ("1".equals(message.get(pos).getIslast())) // 判断已传成功的是最后一条记录
                        {
                            Num = 0;
                            for (int i = 0; i < message.size(); i++) {
                                if (message.get(i).getPicturename()
                                        .equals(message.get(pos).getPicturename())) {
                                    message.get(i).setState("1");// 已上传的标志设为已上传
                                }
                            }
                        }
                        int length = message.size();
                        if (pos == (length - 1)) {//附件上报成功 界面数据恢复到刚开始
                            dissProgressDialog(100, "上传成功");
                            if (message.size() > 0) {
                                message.clear();
                            }
                            if (zipUrlList.size() > 0) {
                                zipUrlList.clear();
                            }
                            if (noZipUrlList.size() > 0) {
                                noZipUrlList.clear();
                            }

                            indexSize = 0;
                            for (int i = 0; i < message.size(); i++) {
                                if ("1".equals(message.get(pos).getState())
                                        && "1".equals(message.get(pos).getIslast())) {
                                    File img2 = new File(message.get(pos)
                                            .getPicturePath());
                                    FileUtil.delete(img2);
                                }
                            }
                            if (listener != null) {
                                listener.upPhotosuccess();
                            }
                        } else {
                            //遍历循环传，不是最后一个，继续上传。
                            cutFileUploaddetail(message.get(pos + 1).getExt(),
                                    message.get(pos + 1).getPicturePath(), message.get(pos + 1).getPicturename(),
                                    projectId, pos + 1, message.get(pos + 1).getStartPos(), message.get(pos + 1)
                                            .getIslast(), message.get(pos + 1).getSize());
                        }
                    } else if ("04".equals(item)) {
                        if (Num < 3) {
                            Num++;
                            for (int i = 0; i < message.size(); i++) {
                                if ("0".equals(message.get(i).getState())) {
                                    cutFileUploaddetail(message.get(i).getExt(),
                                            message.get(i).getPicturePath(), message.get(i).getPicturename(),
                                            projectId, i, message.get(i).getStartPos(), message.get(i)
                                                    .getIslast(), message.get(i).getSize());
                                    break;
                                }
                            }
                        } else {
                            dissProgressDialog(0, "上传失败");
                            Map<String, Object> map = new HashMap<>();
                            map.put("delete", "delete");
                            showToast("上传附件失败！");
                        }
                    } else {
                        failueNum++;
                        if (failueNum > 2) // 一条记录失败两次 则提示网络不正常
                        {
                            dissProgressDialog(0, "网络出错");
                            Map<String, Object> map = new HashMap<>();
                            map.put("delete", "delete");
                            showToast("上传附件失败");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 防止连续弹出吐司
     */
    private void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 上传成功回调
     */
    public interface OnSuccessUpListener {
        void upPhotosuccess();
    }

}
