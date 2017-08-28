package com.kaixuan.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * dialog .view  的辅助处理类
 */

class DialogViewHelper {

    private View mContentView = null;

    //软引用 方式内存泄漏
    public SparseArray<WeakReference<View>> viewkArray;

    public DialogViewHelper(Context mContext, int layoutResId) {
        this();
        mContentView = LayoutInflater.from(mContext).inflate(layoutResId, null);
    }

    public DialogViewHelper() {
        viewkArray = new SparseArray<>();
    }

    /**
     * 设置布局
     **/
    public void setContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    /**
     * 设置文本
     */
    public void setText(int viewId, CharSequence text) {
        //每次都要  findViewById
        TextView tv = getView(viewId);

        if (tv != null) {
            tv.setText(text);
        }

    }

    //获取view，减少findViewById的次数
    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeak = viewkArray.get(viewId);

        View v = null;
        if (viewWeak != null) {
            v = viewWeak.get();
        }

        if (v == null) {
            v = mContentView.findViewById(viewId);
            if (v != null) {
                viewkArray.put(viewId, new WeakReference<>(v));
            }
        }

        return (T) v;
    }

    /**
     * 设置点击事件
     */
    public void setOnClick(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);

        if (view != null) {
            view.setOnClickListener(onClickListener);
        }

    }

    public View getmContentView() {
        return mContentView;
    }
}
