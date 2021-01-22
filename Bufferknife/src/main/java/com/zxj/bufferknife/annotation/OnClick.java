package com.zxj.bufferknife.annotation;


import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnClickListener", listenerType = View.OnClickListener.class, listenerCallbackMethod = "onClick")
public @interface OnClick {

    int[] values();

}
