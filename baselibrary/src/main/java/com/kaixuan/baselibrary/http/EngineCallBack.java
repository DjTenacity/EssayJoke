package com.kaixuan.baselibrary.http;

/**
 *  回调
 */

public interface EngineCallBack<T> {

    //错误
    public void onError(Exception e);

    //成功  返回对象会出问题  成功 data（“”，“”）   失败 data：”“
    public void onSuccess(String result);


    //默认的

    public final EngineCallBack DEFUALT_CALL_BACK=new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };

}
