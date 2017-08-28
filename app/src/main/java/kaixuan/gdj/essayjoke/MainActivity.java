package kaixuan.gdj.essayjoke;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaixuan.baselibrary.ExceptionCrashHandler;
import com.kaixuan.baselibrary.fixBug.FixDexManager;
import com.kaixuan.baselibrary.http.EngineCallBack;
import com.kaixuan.baselibrary.http.HttpUtils;
import com.kaixuan.baselibrary.http.OkhttpEngine;
import com.kaixuan.baselibrary.ioc.CheckNet;
import com.kaixuan.baselibrary.ioc.OnClick;
import com.kaixuan.baselibrary.ioc.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.kaixuan.baselibrary.dialog.AlertDialog;

import kaixuan.framelibrary.BaseSkinActivity;
import kaixuan.framelibrary.DefaultNavigationBar;

public class MainActivity extends BaseSkinActivity {

    @ViewById(R.id.tv)
    TextView tv;
    private AlertDialog dialog;

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar=new DefaultNavigationBar.Builder (this,(ViewGroup) findViewById(R.id.ll_view)).builder();
    }

    @Override
    protected void ininView() {

    }

    @Override
    protected void initData() {
        //aliFix();
        // fixBug();
        //crashFile();
//        HttpUtils httpUtils= new HttpUtils(this);
//
//        httpUtils.exchangeEngine(new OkhttpEngine());
//        httpUtils .get("", null, new EngineCallBack() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onSuccess(String result) {
//
//            }  });
        HttpUtils.with(this).url("").addParams(",","").get().excute(null);


    }

    private void crashFile() {
        //获取上次的异常崩溃信息，上传到服务器
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();

        if (crashFile.exists()) {
            //上传到服务器
            try {
                InputStreamReader fileReader = new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer = new char[1024];
                int len = 0;
                while ((len = fileReader.read(buffer)) != -1) {
                    String str = new String(buffer, 0, len);

                    Log.d("TAG", str);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fixBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (fixFile.exists()) {

            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void aliFix() {
        //        //每次启动的时候  去后台获取差分包 fix.apatch 然后修复本地的bug
//
//        //直接获取本地内存卡里面的 fix.apatch
//
//        File fixFile=new File(Environment.getExternalStorageDirectory(),"fix.apatch");
//        if(fixFile.exists()){
//            //修复bug
//            try {
//                //立即生效，无需重启
//                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);

        //okhttp + Rxjava +retrofit 方式叫做链式调用，并非都是Buidlder设计模式

    }


    @OnClick(R.id.tv2)
    @CheckNet//没网就不执行这个方法，而是进行其他操作
    private void onClick(View view) {
        int a = 2 / 0;// 不会闪退，但是调试会麻烦，要切换到logcat的warn警告层
    }

    @OnClick({R.id.tv, R.id.tv3})
    private void onClick2(View view) {
        int a = 2 / 2;
        //
        dialog = new AlertDialog.Builder(this).setContentView(R.layout.dialog_text)
                .setText(R.id.tv2, "22222").fromBottom(true).fullWidth().show();

        //EditText  et =dialog.getView();特有的操作对象 可以通过getView方法  listView Rv
        dialog.setOnClick(R.id.tv2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //弹出软键盘
    }
}
