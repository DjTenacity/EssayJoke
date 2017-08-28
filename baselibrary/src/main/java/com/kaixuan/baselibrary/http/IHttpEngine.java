package com.kaixuan.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * 网络引擎的规范
 */

public interface IHttpEngine {

    //get请求
    void get(Context context, String url, Map<String, Object> params, EngineCallBack callback);

    //post请求
    void post(Context context,String url, Map<String, Object> params, EngineCallBack callback);


    //下载文件


    //上传文件

    //https添加证书

}
