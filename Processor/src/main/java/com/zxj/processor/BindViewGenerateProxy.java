package com.zxj.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class BindViewGenerateProxy {

    /**
     * 生成.java文件的类名
     */
    private String generateClassName;
    /**
     * 生成.java文件所在的报名
     */
    private String generatePackageName;
    //元素类型
    private TypeElement typeElement;
    /**
     * key为注解生成的id ,如R.id.button = 2131230807 ， value为成员元素
     */
    private Map<Integer, VariableElement> variableElementMap = new HashMap<>();


    public BindViewGenerateProxy(Elements elements, TypeElement classVariableElement) {
        this.typeElement = classVariableElement;
        //获取到类所在的包元素
        PackageElement packageElement = elements.getPackageOf(classVariableElement);
        //根据包元素获取到包名
        String packName = packageElement.getQualifiedName().toString();
        //根据类元素获取到类名
        String classSimpleName = classVariableElement.getSimpleName().toString();
        this.generatePackageName = packName;
        this.generateClassName = classSimpleName + "_ViewBinding";
    }

    public void putVariableElement(int resourceId, VariableElement variableElement) {
        variableElementMap.put(resourceId, variableElement);
    }

    /**
     * 利用com.squareup.javapoet 生成方法
     *
     * @return
     */
    private MethodSpec generateMethod() {
        ClassName className = ClassName.bestGuess(typeElement.getQualifiedName().toString());
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("initFindViewById")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(className, "classNameContext");

        for (int resourceId : variableElementMap.keySet()) {
            //根据资源id获取到对应的元素信息
            VariableElement variableElement = variableElementMap.get(resourceId);
            //获取到变量的名称
            String variableName = variableElement.getSimpleName().toString();
            //获取到成员变量的类型（全类名）
            String variableType = variableElement.asType().toString();
            methodBuilder.addCode("classNameContext." + variableName + " = " + "(" + variableType + ")((android.app.Activity)classNameContext).findViewById(" + resourceId + ");");
        }
        return methodBuilder.build();
    }

    /**
     * 利用com.squareup.javapoet 生成类
     * @return
     */
    public TypeSpec generateClass(){
        TypeSpec generateClass = TypeSpec.classBuilder(generateClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateMethod())
                .build();
        return generateClass;
    }

    public String getPackageName() {
        return generatePackageName;
    }
}
