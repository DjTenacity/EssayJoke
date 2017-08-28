package com.kaixuan.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kaixuan.baselibrary.ioc.ViewUtils;

/**
 * 整个应用的 BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //针对MVC的模式，mvp有区别
        //设置布局layout
        setContentView();
        ViewUtils.inject(this);
        //初始化头部
        initTitle();
        //初始化界面
        ininView();
        //初始化数据
        initData();
    }

    protected abstract void initTitle();

    protected abstract void ininView();

    protected abstract void initData();

    protected abstract void setContentView();

    /**
     * 启动activity
     */
    protected void starActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }
    /**findViewById*/
    protected <T extends View> T viewById(int viewId) {
        return (T) findViewById(viewId);
    }
    //只能放一些常用通用的方法，几乎每个activity都用到、
    //如果用的较多的话，可以放到工具类里面

    //activity继承baseActivity，就会加载到内存，方法也会存在内存，
    //最终会是内存地址

    //永远预留一层，性能问题
}
