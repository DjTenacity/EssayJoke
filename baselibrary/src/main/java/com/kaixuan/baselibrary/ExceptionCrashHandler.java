package com.kaixuan.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常捕捉类
 * 单例的设计模式的
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionCrashHandler mCrashHandle;
    private final String TAG = "ExceptionCrash";

    public static ExceptionCrashHandler getInstance() {

        //解决多并发的问题
        synchronized (ExceptionCrashHandler.class) {
            if (mCrashHandle == null) {
                mCrashHandle = new ExceptionCrashHandler();
            }
        }
        return mCrashHandle;
    }

    //获取应用信息
    private Context mContext;
    //获取系统默认的
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public void init(Context context) {
        this.mContext = context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        //  全局异常
        Log.e(TAG, "异常了");

        //写入到本地文件   Exception  ex  当前版本号

        //崩溃的详细信息

        //应用信息 包名 版本号

        //手机信息

        //上传问题，上传文件不在这里处理，等应用再次启动的时候再上传
        String crashFileName = saveInfoToSD(e);

        Log.e(TAG, "fileName --> " + crashFileName);

        // 3. 缓存崩溃日志文件
        cacheCrashFile(crashFileName);
        // 系统默认处理
        mDefaultExceptionHandler.uncaughtException(thread, e);
    }

    /**
     * 缓存崩溃日志文件
     *
     * @param fileName
     */
    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", fileName).commit();
    }

    /**
     * 获取崩溃文件名称
     *
     * @return
     */
    public File getCrashFile() {
        String crashFileName = mContext.getSharedPreferences("crash",
                Context.MODE_PRIVATE).getString("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDcard中
     *
     * @param ex
     * @return
     */
    private String saveInfoToSD(Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext)
                .entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        //崩溃的详细信息
        sb.append(obtainExceptionInfo(ex));

        //保存文件，手机应用的目录，并没有拿手机的sd卡目录，6.0以上需要动态申请权限（这时候已经崩掉了）
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(mContext.getFilesDir() + File.separator + "crash"
                    + File.separator);

            // 先删除之前的异常信息
            if (dir.exists()) {
                //删除该目录下的所有子文件
                deleteDir(dir);
            }

            // 再从新创建文件夹
            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                fileName = dir.toString()
                        + File.separator
                        + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 返回当前日期根据格式
     **/
    private String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }


    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());
        return map;
    }


    /**
     * Cell phone information
     * 反射
     *
     * @return
     */
    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            //利用反射获取类的所有属性
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                //所有的都能用
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            // 递归删除目录中的子目录下
            for (File child : children) {
                child.delete();
            }
        }
        // 目录此时为空，可以删除
        return true;
    }


}
