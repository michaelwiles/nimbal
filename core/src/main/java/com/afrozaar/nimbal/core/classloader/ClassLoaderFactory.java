package com.afrozaar.nimbal.core.classloader;

import com.afrozaar.nimbal.core.ErrorLoadingArtifactException;
import com.afrozaar.nimbal.core.IRegistry;
import com.afrozaar.nimbal.core.ModuleInfo;

import java.net.URL;
import java.util.Arrays;

public class ClassLoaderFactory {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ClassLoaderFactory.class);

    private IRegistry registry;

    public ClassLoaderFactory(IRegistry registry) {
        this.registry = registry;
    }

    public ClassLoader getClassLoader(String artifactId, ModuleInfo moduleInfo, URL[] urls) throws ErrorLoadingArtifactException {
        String parentName = moduleInfo.parentModule();

        if (parentName != null && registry.getClassLoader(parentName) == null) {
            throw new ErrorLoadingArtifactException("no parent class loader found for parent {}", parentName);
        }
        LOG.debug("creating class loader for {}, artifactId:{} with parent ='{}' ringFenceBlackList:{} and jar list {}", moduleInfo.name(), artifactId,
                moduleInfo.parentModule(), moduleInfo.ringFenceFilters(), Arrays.asList(urls));

        URLClassLoaderExtension classLoader = new URLClassLoaderExtension(urls, parentName != null ? registry.getClassLoader(parentName)
                : this.getClass().getClassLoader(), artifactId);
        classLoader.setRingFencedFilters(moduleInfo.ringFenceFilters());

        return classLoader;
    }
}
