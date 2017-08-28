package kaixuan.gdj.essayjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

//import com.alipay.euler.andfix.patch.PatchManager;
import com.kaixuan.baselibrary.ExceptionCrashHandler;
import com.kaixuan.baselibrary.fixBug.FixDexManager;
import com.kaixuan.baselibrary.http.HttpUtils;
import com.kaixuan.baselibrary.http.OkhttpEngine;

/**
 * 程序入口
 */

public class BaseApplication extends Application {

    //public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //设置全局异常捕捉类
        //ExceptionCrashHandler.getInstance().init(this);

        HttpUtils.init(new OkhttpEngine());

//         初始化阿里的热修复
//        mPatchManager = new PatchManager(this);
//
//        try {
//            //初始化版本，获取当前应用版本号
//            PackageManager packageManager = this.getPackageManager();
//            PackageInfo info = packageManager.getPackageInfo(this.getPackageName(), 0);
//            String version=info.versionName;
//            mPatchManager.init(version);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        //加载之前的 apach包
//        mPatchManager.loadPatch();

//        try {
//            FixDexManager fixDexManager = new FixDexManager(this);
//            fixDexManager.loadFixDex();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
