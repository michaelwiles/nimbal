package com.afrozaar.nimbal.annotations;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
public @interface Module {

    int order() default Integer.MIN_VALUE;

    String name() default "";

    String value() default "";

    /**
     * The module this module depends on (will bcome the parent module, both
     * classloader and will be included in the chain) and bean factory also if parentModuleClaseses only is false
     * <p>
     * If this is specified the parentModuleClassesOnly will be ignored
     * </p>
     */
    String parentModule() default "";

    /**
     * specifies that that parent module should only load classes and not bean context 
     */
    boolean parentModuleClassesOnly() default false;

    String[] ringFenceClassBlackListRegex() default {};
}