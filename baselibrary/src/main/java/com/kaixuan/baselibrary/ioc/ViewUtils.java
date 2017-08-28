package com.kaixuan.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/7/24.
 */

public class ViewUtils {

    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    //
    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    //兼容上面三种方法          object其实就是反射需要执行的类
    private static void inject(ViewFinder finder, Object object) {
        injectFiled(finder, object);
        injectEvent(finder, object);
    }

    /**
     * 注入属性
     **/
    private static void injectFiled(ViewFinder finder, Object object) {
        //1，获取类里面所有的属性，反射
        Class<?> clazz = object.getClass();
        //获取所有属性，包括私有和公有
        Field[] fields = clazz.getDeclaredFields();

        //2,获取ViewById的里面的vaule值
        for (Field field : fields) {
            //现在拿到了所有属性
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                //获取注解里面的id值--->R.id.tv
                int viewId = viewById.value();

                //3，findViewById 找到VIew

                View view = finder.findViewByid(viewId);

                if (view != null) {
                    //4,动态的注入找到的view ，所有的属性都可以包括
                    field.setAccessible(true);

                    try {
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }

        }


    }

    /**
     * 注入事件，获取类里面的方法
     **/
    private static void injectEvent(ViewFinder finder, Object object) {
        //1，获取类里面的方法
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        //2,获取ViewById里面的value值
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            //activity中的onCreate方法也会获得
            if (onClick != null) {

                int[] viewIds = onClick.value();
                for (int viewId : viewIds) {
                    //3，findViewById 找到VIew
                    View view = finder.findViewByid(viewId);

                    //扩展功能 检测网络

                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;


                    //4，view.setOnclickListener
                    if (view != null) {
                        view.setOnClickListener(new DeclaredOnClickListener(method, object, isCheckNet));
                    }

                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Object object;
        private Method method;
        private boolean isCheckNet;

        public DeclaredOnClickListener(Method method, Object object, boolean isCheckNet) {
            this.method = method;
            this.object = object;
            this.isCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View v) {
            //需不需要检测网络
            if (isCheckNet) {
                //需要
                if (!NetWorkAvailable(v.getContext())) {//没网
                    Toast.makeText(v.getContext(), "断网了大兄弟.请检测网络", Toast.LENGTH_SHORT).show();
                    return;
                }

            }


            //点击调用该方法
            try {
                //所有方法都可以
                method.setAccessible(true);
                //5,反射执行方法
                method.invoke(object, v);
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    //onClick方法可无参
                    method.invoke(object, null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static boolean NetWorkAvailable(Context context) {
        //得到连接管理对象
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
