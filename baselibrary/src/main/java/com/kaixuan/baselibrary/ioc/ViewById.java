package com.kaixuan.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * view 注解的 Annotation
 */

//@Target  代表Annotation的位置   FIELD属性  TYPE类上的CONSTRUCTOR构造函数上
@Target(ElementType.FIELD)
//什么时候生效    CLASS编译时   RUNNING运行时   SOURCE源码资源
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    //————>@VIewById(R.id.XXXX)
    int value();
}
