package com.kaixuan.baselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/29.
 */

class AlertController {
    AlertDialog mDialog;
    Window mWindow;
    private DialogViewHelper viewHelper;

    public AlertController(AlertDialog alertDialog, Window window) {
        this.mDialog = mDialog;
        this.mWindow = window;
    }

    public AlertDialog getmDialog() {
        return mDialog;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    /**
     * 获取dialog的window
     */
    public Window getmWindow() {
        return mWindow;
    }

    //设置文本
    public void setText(int viewId, CharSequence text) {

        viewHelper.setText(viewId, text);
    }


    //获取view，减少findViewById的次数
    public <T extends View> T getView(int viewId) {
        return viewHelper.getView(viewId);

    }

    /**
     * 设置点击事件
     */
    public void setOnClick(int viewId, View.OnClickListener onClickListener) {
        viewHelper.setOnClick(viewId, onClickListener);
    }


    public static class AlertParams {

        public Context mContext;
        public int mThemeResId;

        //点击空白是否能够取消
        public boolean cancelable = true;
        //dialog Cancel监听
        public DialogInterface.OnCancelListener onCancelListener;
        //dialog Dismiss监听
        public DialogInterface.OnDismissListener onDismissListener;
        //dialog 按键监听
        public DialogInterface.OnKeyListener onKeyListener;

        //布局
        public View mView;
        //布局layout id
        public int mViewLayoutResId;

        //SparseArray 类似于HashMap，更加高效，key是Integer

        //存放字体的修改
        public SparseArray<CharSequence> textArray;
        //存放点击事件
        public SparseArray<View.OnClickListener> clickArray;

        public int mGravity = Gravity.CENTER;
        //动画
        public int mAimation = 0;

        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         **/
        public void apply(AlertController mAlert) {

            //设置参数  DialogViewHelper
            DialogViewHelper viewHelper = null;

            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }
            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }
            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            //设置COntroller的辅助类
            mAlert.setViewHelper(viewHelper);

            //设置布局
            mAlert.getmDialog().setContentView(viewHelper.getmContentView());

            //设置文本和点击

            int textArraySize = textArray.size();

            for (int i = 0; i < textArraySize; i++) {
                mAlert.setText(textArray.keyAt(i), textArray.valueAt(i));

            }

            int clickArraySize = clickArray.size();

            for (int i = 0; i < clickArraySize; i++) {
                mAlert.setOnClick(clickArray.keyAt(i), clickArray.valueAt(i));

            }

            //配置自定义的效果  全屏 弹出 动画
            Window window = mAlert.getmWindow();

            //设置位置
            window.setGravity(mGravity);

            //设置动画
            if (mAimation != 0) {
                window.setWindowAnimations(mAimation);
            }
            //设置宽高
            WindowManager.LayoutParams params=window.getAttributes();
            params.width=mWidth;
            params.height=mHeight;

            window.setAttributes(params);
        }

    }

}
