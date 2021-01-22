package com.zxj.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.zxj.annotations.BindView;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * 自定义注解器，在程序编译的时候，会执行注解器Processor的所有子类，从而扫描程序的所有注解
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    /**
     * 元素工具类
     */
    private Elements elements;
    /**
     * key为所在的类全名，value为所在该类中的成员元素信息集合
     */
    private Map<String,BindViewGenerateProxy> map = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elements = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //返回支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        map.clear();
        //获取到程序中所有类中的BindView注解
        Set<? extends Element> bindViewAnnotates = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : bindViewAnnotates){
            //因为我们自定的BindView注解只能作用在成员变量上，所以这里的元素就是变量元素
            VariableElement bindViewVariableElement = (VariableElement) element;
            //根据成员元素获取成员元素所在的类元素
            TypeElement classVariableElement = (TypeElement) bindViewVariableElement.getEnclosingElement();
            //获取到类元素的全类名
            String classFullName = classVariableElement.getQualifiedName().toString();
            //获取到元素类的包装类
            BindViewGenerateProxy bindViewGenerateProxy = map.get(classFullName);
            if (bindViewGenerateProxy == null){
                bindViewGenerateProxy = new BindViewGenerateProxy(elements,classVariableElement);
                map.put(classFullName,bindViewGenerateProxy);
            }

            BindView bindView = bindViewVariableElement.getAnnotation(BindView.class);
            int resourceId = bindView.value();
            bindViewGenerateProxy.putVariableElement(resourceId,bindViewVariableElement);
        }

        //通过javapoet生成Java文件
        for (String classFullName:map.keySet()){
            BindViewGenerateProxy bindViewGenerateProxy = map.get(classFullName);
            JavaFile javaFile = JavaFile.builder(bindViewGenerateProxy.getPackageName(), bindViewGenerateProxy.generateClass()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
