package com.sinosoft.ops.cimp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileUpload {

    /**
     * 是否做摘要
     */
    boolean digest() default false;

    /**
     * 是否自动保存
     */
    boolean autoSave() default true;
}