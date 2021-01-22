package com.zxj.bufferknife.annotation;


import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnLongClickListener", listenerType = View.OnLongClickListener.class, listenerCallbackMethod = "onLongClick")
public @interface OnLongClick {

    int[] values();

}
