package com.kaixuan.baselibrary.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 自己的一套实现
 */

public class HttpUtils {
    //直接带参数---->链式调用

    private String mUrl;
    private Map<String, Object> mParams;

    //请求方式
    private int mType = GET_TYPE;
    private static final int GET_TYPE = 0x0010;
    private static final int POST_TYPE = 0x0011;

    //上下文
    private Context mContext;

    public HttpUtils(Context context) {
        mContext = context;
        //ArrayMap 与 HashMap 的区别
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }


    //添加参数
    public HttpUtils addParams(String key, Object vaule) {
        mParams.put(key, vaule);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    //添加回调,执行
    public void excute(EngineCallBack callBack) {
        if (callBack == null) {
            callBack = EngineCallBack.DEFUALT_CALL_BACK;
        }
        //判断执行方法
        if (mType == POST_TYPE) {
            post(mContext, mUrl, mParams, callBack);
        }
        if (mType == GET_TYPE) {
            get(mContext, mUrl, mParams, callBack);
        }


    }

    public void excute() {
        excute(null);
    }


    //默认 OkhttpEngine
    private static IHttpEngine mHttpEngine = new OkhttpEngine();

    //在application中初始化引擎
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    //每次可以自带引擎
    public void exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    private void get(Context context, String url, Map<String, Object> params, EngineCallBack callback) {
        mHttpEngine.get(context, url, params, callback);
    }

    private void post(Context context, String url, Map<String, Object> params, EngineCallBack callback) {
        mHttpEngine.post(context, url, params, callback);
    }

    //拼接参数
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() < 0) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        } else if (!url.endsWith("&")) {
            sb.append("&");
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        sb.deleteCharAt(sb.length() - 1);

        System.out.println(sb.toString());

        return sb.toString();
    }
}
