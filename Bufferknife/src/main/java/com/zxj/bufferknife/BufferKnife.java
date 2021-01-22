package com.zxj.bufferknife;

import android.app.Activity;
import android.view.View;

import com.zxj.bufferknife.annotation.EventBase;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BufferKnife {

    public static void inject(Activity activity) {
        //给成员变量注解
        injectView(activity);
        //给方法注册事件
        injectEvent(activity);
    }

    private static void injectView(Activity activity) {
        Class clazz = activity.getClass();
        try {
            Class generateClass = Class.forName(clazz.getName() + "_ViewBinding");
            Method method = generateClass.getMethod("initFindViewById", activity.getClass());
            method.invoke(generateClass.newInstance(), activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void injectEvent(Activity activity) {
        Class clazz = activity.getClass();
        //反射获取该类的所有方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            //获取方法上的所有方法
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //获取到当前注解的类对象
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //根据获取到的类对象再获取类对象上的注解
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }
                String listenerSetter = eventBase.listenerSetter();
                Class<?> listenerType = eventBase.listenerType();
                // String listenerCallbackMethod = eventBase.listenerCallbackMethod();

                try {
                    //反射获取到点击注解上的值
                    Method valueMethod = annotationType.getDeclaredMethod("values");
                    int[] resourceIds = (int[]) valueMethod.invoke(annotation);

                    /**
                     *
                     *     button.setOnClickListener(new View.OnClickListener() {
                     *             @Override
                     *             public void onClick(View v) {
                     *                 Toast.makeText(MainActivity.this,"aaaa",Toast.LENGTH_SHORT).show();
                     *             }
                     *         });
                     *
                     *
                     *
                     */

                    for (int resourceId : resourceIds) {
                        //根据点击注解上的值反射获取到对应的view
                        Method findViewByIdMethod = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewByIdMethod.invoke(activity, resourceId);
                        //返回获取到view的监听方法
                        Method listenerSetterMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        listenerSetterMethod.invoke(view, Proxy.newProxyInstance(view.getClass().getClassLoader(), new Class[]{listenerType}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method methodObject, Object[] args) throws Throwable {
                                return method.invoke(activity, args);
                            }
                        }));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
