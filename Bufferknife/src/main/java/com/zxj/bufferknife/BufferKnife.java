package com.zxj.bufferknife;

import android.app.Activity;

import java.lang.reflect.Method;

public class BufferKnife {

    public static void inject(Activity activity){
        Class clazz =   activity.getClass();
        try {
            Class generateClass = Class.forName(clazz.getName()+"_ViewBinding");
            Method method = generateClass.getMethod("initFindViewById",activity.getClass());
            method.invoke(generateClass.newInstance(),activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
