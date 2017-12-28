package com.afrozaar.nimbal.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.afrozaar.nimbal.core.ContextFactory.ParentContext;
import com.afrozaar.nimbal.core.classloader.ClassLoaderFactory;

import org.springframework.context.ApplicationContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContextFactoryTest {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ContextFactoryTest.class);

    @InjectMocks
    private ContextFactory contextFactory;

    @InjectMocks
    private ClassLoaderFactory classLoaderFactory;

    @Mock
    private IRegistry registry;

    @Mock
    private ApplicationContext applicationContext;

    @Test
    public void DefaultParentContext() throws ErrorLoadingArtifactException {
        contextFactory.setApplicationContext(applicationContext);
        ParentContext parentContext = contextFactory.getParentContext(new ModuleInfo());

        assertThat(parentContext.getApplicationContext()).isSameAs(applicationContext);
        assertThat(parentContext.getClassLoader()).isSameAs(this.getClass().getClassLoader());
    }

    @Test
    public void CheckErrorWhenParentNotPresent() {
        contextFactory.setApplicationContext(applicationContext);
        ModuleInfo module = new ModuleInfo();
        module.setName("bar");
        module.setParentModule("foo");
        ParentContext parentContext;
        try {
            parentContext = contextFactory.getParentContext(module);
            failBecauseExceptionWasNotThrown(ErrorLoadingArtifactException.class);
        } catch (ErrorLoadingArtifactException e) {
            assertThat(e.getMessage()).isEqualTo("module bar required parent module foo but is not present.");
        }
    }

    @Test
    public void WhenParentContext() throws ErrorLoadingArtifactException {
        contextFactory.setApplicationContext(applicationContext);

        ModuleInfo module = new ModuleInfo();
        module.setName("bar");
        String parentModule = "foo";
        module.setParentModule(parentModule);

        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);
        when(registry.getContext(parentModule)).thenReturn(mockApplicationContext);
        ClassLoader mockClassLoader = mock(ClassLoader.class);
        when(registry.getClassLoader(parentModule)).thenReturn(mockClassLoader);

        ParentContext parentContext = contextFactory.getParentContext(module);

        assertThat(parentContext.getApplicationContext()).isSameAs(mockApplicationContext);
        assertThat(parentContext.getClassLoader()).isSameAs(mockClassLoader);

    }

    @Test
    public void WhenParentClassLoaderOnly() throws ErrorLoadingArtifactException {
        contextFactory.setApplicationContext(applicationContext);

        ModuleInfo module = new ModuleInfo();
        module.setName("bar");
        String parentModule = "foo";
        module.setParentClassLoaderOnly(true);
        module.setParentModule(parentModule);

        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);
        when(registry.getContext(parentModule)).thenReturn(mockApplicationContext);
        ClassLoader mockClassLoader = mock(ClassLoader.class);
        when(registry.getClassLoader(parentModule)).thenReturn(mockClassLoader);

        ParentContext parentContext = contextFactory.getParentContext(module);

        assertThat(parentContext.getApplicationContext()).isSameAs(applicationContext);
        assertThat(parentContext.getClassLoader()).isSameAs(mockClassLoader);
    }
    
    

}