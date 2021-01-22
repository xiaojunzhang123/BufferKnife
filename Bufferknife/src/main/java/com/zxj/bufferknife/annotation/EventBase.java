package com.zxj.bufferknife.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface EventBase {

    /**
     * 监听的方法
     *
     * @return
     */
    String listenerSetter();

    /**
     * 监听方法的参数类型
     *
     * @return
     */
    Class<?> listenerType();

    /**
     * 监听方法中参数类型里的回调方法
     *
     * @return
     */
    String listenerCallbackMethod();
}
