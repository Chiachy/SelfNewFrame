package com.common.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.common.base.bean.PubData;
import com.common.base.bean.UserInfo;
import com.common.base.net.HttpResponseCallBack;
import com.common.base.net.HttpUtil;
import com.common.base.utils.base.XmlUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HSK° on 2018/12/4.
 * --function: 全局异常捕获，异常信息上传至服务器
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/CrashRLibs/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;
    @SuppressLint("SimpleDateFormat")
    private java.text.DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    private CrashHandler() {
    }

    public static CrashHandler getsInstance() {
        return sInstance;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //导出异常信息到SD卡中
        //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决bug
        uploadExceptionToServer(ex);
        ex.printStackTrace();
        //如果系统提供默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        try {
            Thread.sleep(2000);
            Toast.makeText(mContext, "程序出错了~", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.e(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new Date(current));
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        //Android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.print(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.print(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    private void uploadExceptionToServer(Throwable ex) {
        //将异常信息发送到服务器
        Map<String, Object> exceptionInfo = getExceptionInfo(ex);
        saveCrashInfo2File(ex, exceptionInfo);
    }


    private Map<String, Object> getExceptionInfo(Throwable ex) {
        Map<String, Object> mapParam = new HashMap<String, Object>();
        try {
            mapParam.put("sqlKey", "sys_save_exception_info_sql");
            mapParam.put("sqlType", "proc");
            mapParam.put("QSQLKEY", "CLIENT_NO_SQL");// 异常出错的sqlkey
            mapParam.put("QEXCEPTION_HAPPEND_FROM", "200");// android
            mapParam.put("QEXCEPTION_PHONE_TYPE", android.os.Build.MODEL);// 设备型号
            mapParam.put("QOS_VERSION", android.os.Build.VERSION.RELEASE);// 异常操作系统类型
            mapParam.put("QEXCEPTION_CLASS", ex.getClass().getName());
            StackTraceElement[] trace = ex.getStackTrace();
            for (StackTraceElement stackTrace : trace) {// 获得异常相关信息
                String className = stackTrace.getClassName();
                if (className.contains("com.common.base")) {
                    mapParam.put("QEXCEPTION_HAPPEND_CLASS",
                            stackTrace.getFileName());
                    mapParam.put("QEXCEPTION_HAPPEND_METHOD",
                            stackTrace.getMethodName());
                    mapParam.put("QEXCEPTION_HAPPEN_LINENUMBER",
                            stackTrace.getLineNumber());
                    break;
                }
            }
            // 异常信息
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            mapParam.put("QEXCEPTION_DETAIL_INFO", result);
            // 异常账号信息
            UserInfo userinfo = DbManager.getUserinfo();

            if (userinfo != null) {
                String compId = userinfo.getCompId();
                String userId = userinfo.getUserId();
                mapParam.put("QUSER_ID", userId);
                mapParam.put("QCOMPANY_ID", compId);
            }
            mapParam.put("QCREATEDATETIME", formatter.format(new Date()));
            // 获得异常产生的版本号
            collectDeviceInfo(mContext, mapParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapParam;
    }

    /**
     * 保存异常信息
     * 上传至服务器，不成功保存本地
     */
    private void saveCrashInfo2File(final Throwable ex, Map<String, Object> mapParam) {
        // 将异常数据拼成String传到后台调用
        String reqxml = XmlUtil.obj2JSON(mapParam);
        HttpUtil.getInstance().updateData(reqxml, new HttpResponseCallBack() {
            @Override
            public void onResponse(Object data, int what) {
                PubData pubData = (PubData) data;
                if (pubData == null || "99".equals(pubData.getCode())) {// 保存数据失败，将数据保存到本地文件中
                    try {
                        dumpExceptionToSDCard(ex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }


    /**
     * 异常产生的版本号
     */
    private void collectDeviceInfo(Context ctx, Map<String, Object> mapParam) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "MP"
                        : pi.versionName;
                mapParam.put("QEXCEPTION_VERSION_NUMBER", versionName);
            } else {
                mapParam.put("QEXCEPTION_VERSION_NUMBER", "MP");

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
        }
    }
}

