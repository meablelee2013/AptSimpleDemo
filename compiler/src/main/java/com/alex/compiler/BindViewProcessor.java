package com.alex.compiler;

import com.alex.annotation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    /**
     * 工具类,可以从init方法的ProcessingEnvironment中获取
     */
    private Elements elementUtils;
    /**
     * 缓存所有子Element
     * key:父Element类名
     * value:子Element
     */
    private HashMap<String, List<Element>> cacheElements = null;
    /**
     * 缓存所有父Element
     * key:父Element类名
     * value:父Element
     */
    private HashMap<String, Element> cacheAllParentElements = null;

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            addElementToCache(element);
        }
        if (cacheElements == null || cacheElements.size() == 0) {
            return true;
        }
        for (String parentElementName : cacheElements.keySet()) {
            //判断一下获取到的parent element是否是类
            try {
                //使用JavaPoet构造一个方法
                MethodSpec.Builder bindViewMethodSpec = MethodSpec.methodBuilder("bindView")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(void.class)
                        .addParameter(ClassName.get(cacheAllParentElements.get(parentElementName).asType())
                                , "targetActivity");

                List<Element> childElements = cacheElements.get(parentElementName);
                if (childElements != null && childElements.size() != 0) {
                    for (Element childElement : childElements) {
                        BindView bindView = childElement.getAnnotation(BindView.class);
                        //使用JavaPoet对方法内容进行添加
                        bindViewMethodSpec.addStatement(
                                String.format("targetActivity.%s = (%s) targetActivity.findViewById(%s)"
                                        , childElement.getSimpleName()
                                        , ClassName.get(childElement.asType()).toString()
                                        , bindView.id()));
                    }
                }

                //构造一个类,以Bind_开头
                TypeSpec typeElement = TypeSpec.classBuilder("Bind_"
                        + cacheAllParentElements.get(parentElementName).getSimpleName())
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(bindViewMethodSpec.build())
                        .build();

                //进行文件写入
                JavaFile javaFile = JavaFile.builder(
                        getPackageName((TypeElement) cacheAllParentElements.get(parentElementName))
                        , typeElement).build();
                javaFile.writeTo(processingEnv.getFiler());

            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }


        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindView.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 缓存父Element对应的所有子Element
     * 缓存父Element
     *
     * @param childElement
     */
    private void addElementToCache(Element childElement) {
        if (cacheElements == null) {
            cacheElements = new HashMap<>();
        }

        if (cacheAllParentElements == null) {
            cacheAllParentElements = new HashMap<>();
        }
        //父Element类名
        String parentElementName = null;
        parentElementName = ClassName.get(childElement.getEnclosingElement().asType()).toString();

        if (cacheElements.containsKey(parentElementName)) {
            List<Element> childElements = cacheElements.get(parentElementName);
            childElements.add(childElement);
        } else {
            ArrayList<Element> childElements = new ArrayList<>();
            childElements.add(childElement);
            cacheElements.put(parentElementName, childElements);
            cacheAllParentElements.put(parentElementName, childElement.getEnclosingElement());
        }
    }

}
