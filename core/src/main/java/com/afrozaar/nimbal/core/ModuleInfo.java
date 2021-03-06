/*******************************************************************************
 * Nimbal Module Manager 
 * Copyright (c) 2017 Afrozaar.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution and is available at https://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package com.afrozaar.nimbal.core;

import com.afrozaar.nimbal.annotations.Module;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import org.springframework.context.annotation.Configuration;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModuleInfo {
    private Integer order;
    private String name;
    private String parentModule;
    private boolean parentModuleClassesOnly;
    private List<String> ringFencedFilters = Collections.emptyList();
    private String moduleClass;

    private String artifactId;

    public ModuleInfo() {
        super();
    }

    public ModuleInfo(Module module, Class<?> moduleClass) {
        this.order = module.order() == Integer.MIN_VALUE ? null : module.order();

        List<String> names = Lists.newArrayList(module.name(), module.value(), moduleClass.getSimpleName());

        this.name = names.stream().filter(StringUtils::isNotBlank).findFirst().get();
        this.parentModule = StringUtils.stripToNull(module.parentModule());
        this.parentModuleClassesOnly = module.parentModuleClassesOnly();
        this.ringFencedFilters = Arrays.asList(module.ringFenceClassBlackListRegex());
        this.moduleClass = moduleClass.getName();
    }

    public ModuleInfo(Configuration annotation, Class<?> clazz) {
        this.name = StringUtils.stripToNull(annotation.value());
        if (this.name == null) {
            this.name = clazz.getSimpleName();
        }
        this.moduleClass = clazz.getName();
    }

    public ModuleInfo(String name) {
        this.name = name;
    }

    public Integer order() {
        return order;
    }

    public String name() {
        return name;
    }

    public String parentModule() {
        return parentModule;
    }

    public List<String> ringFenceFilters() {
        return ringFencedFilters;
    }

    public String moduleClass() {
        return moduleClass;
    }

    @Override
    public String toString() {
        return "ModuleInfo [name=" + name + ", parentModule=" + parentModule + ", order=" + order + ", ringFencedFilters=" +
                ringFencedFilters
                + ", moduleClass=" + moduleClass + "]";
    }

    public boolean parentModuleClassesOnly() {
        return parentModuleClassesOnly;
    }

    public boolean isReloadRequired() {
        return parentModule != null || !ringFencedFilters.isEmpty();
    }

    public void setName(String name) {
        this.name = name;
    }

    @VisibleForTesting
    public void setParentModule(String parentModule) {
        this.parentModule = parentModule;

    }

    public void setParentClassLoaderOnly(boolean parentClassLoaderOnly) {
        this.parentModuleClassesOnly = parentClassLoaderOnly;
    }

}
