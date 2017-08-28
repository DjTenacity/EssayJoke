package com.kaixuan.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * FindViewById的辅助类
 */

public class ViewFinder {
    Activity mActivity;
    View mView;

    public ViewFinder(View view) {
        this.mView=view;
    }

    public ViewFinder(Activity activity) {
        this.mActivity=activity;
    }

    public View findViewByid(int viewId){
        return mActivity==null?mActivity.findViewById(viewId):mView.findViewById(viewId);
    }
}
