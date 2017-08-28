package com.kaixuan.baselibrary.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 头部的基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    private View mNavigationView;

    public AbsNavigationBar(P params) {
        this.mParams = params;

        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }


    public void setText(int viewId, String title) {
        TextView tv = findViewById(viewId);

        if (!TextUtils.isEmpty(title)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(title);
        }
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
    }


    public <T extends View> T findViewById(int viewId) {
        return (T) mNavigationView.findViewById(viewId);
    }

    /**
     * 绑定和创建View
     **/
    private void createAndBindView() {
        if (mParams.mParent == null) {//获取activity的根布局

            ViewGroup activityRoot = (ViewGroup) ((Activity) (mParams.mContext))
                    //.findViewById(android.R.id.content);
                    .getWindow().getDecorView();

            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }

        if (mParams.mParent == null) {
            return;
        }

        //创建View
        mNavigationView = LayoutInflater.from(mParams.mContext)
                .inflate(bindLayoutId(), mParams.mParent, false);
        //添加
        mParams.mParent.addView(mNavigationView, 0);

        applyView();

    }

    @Override
    public abstract int bindLayoutId();

    @Override
    public abstract void applyView();

    //builder  模式  AbsNavigationBar Builder 参数、

    public abstract static class Builder {

        public Builder(Context context, ViewGroup parent) {

        }

        public abstract AbsNavigationBar builder();

        public static class AbsNavigationParams {

            public Context mContext;
            public ViewGroup mParent;


            public AbsNavigationParams(Context context, ViewGroup parent) {

                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
