package kaixuan.framelibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kaixuan.baselibrary.navigationbar.AbsNavigationBar;

/**
 * Created by Administrator on 2017/7/30.
 */

public class DefaultNavigationBar extends
        AbsNavigationBar<DefaultNavigationBar.Builder.DefaulsNavigationBarParams> {

    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaulsNavigationBarParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.layout_title_bar;
    }

    @Override
    public void applyView() {
        //绑定参数，绑定效果
        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_title, getParams().mTitle);

        setOnClickListener(R.id.right_title, getParams().rightClickListener);

        //做编写一个默认的  finish()
        setOnClickListener(R.id.back, getParams().leftClickListener);
    }

    public static class Builder extends AbsNavigationBar.Builder {

        DefaulsNavigationBarParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaulsNavigationBarParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            P = new DefaulsNavigationBarParams(context, null);
        }

        @Override
        public DefaultNavigationBar builder() {

            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(P);
            return defaultNavigationBar;
        }

        //设置所有效果
        public DefaultNavigationBar.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public DefaultNavigationBar.Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        public DefaultNavigationBar.Builder setLeftText(String leftText) {
            P.mLeftText = leftText;
            return this;
        }

        public DefaultNavigationBar.Builder setRightIcon(String rightIcon) {
            P.mRightIcon = rightIcon;
            return this;
        }

        public DefaultNavigationBar.Builder setLeftIcon(String leftIcon) {
            P.mLeftIcon = leftIcon;
            return this;
        }

        /**
         * 设置右边监听
         **/
        public DefaultNavigationBar.Builder setRightClickLinstener(View.OnClickListener rightClickListener) {
            P.rightClickListener = rightClickListener;
            return this;
        }

        /**
         * 设置左边监听
         **/
        public DefaultNavigationBar.Builder setLeftClickLinstener(View.OnClickListener leftClickListener) {
            P.leftClickListener = leftClickListener;
            return this;
        }

        public static class DefaulsNavigationBarParams extends
                AbsNavigationBar.Builder.AbsNavigationParams {

            //放置所有效果
            public String mTitle;
            public String mLeftText;
            public String mRightText;
            public String mRightIcon;
            public String mLeftIcon;
            public View.OnClickListener rightClickListener;
            public View.OnClickListener leftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) mContext).finish();
                }
            };

            public DefaulsNavigationBarParams(Context context, ViewGroup parent) {
                super(context, parent);
            }


        }
    }
}
