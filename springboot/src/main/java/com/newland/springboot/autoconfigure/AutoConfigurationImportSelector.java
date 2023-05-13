package com.newland.springboot.autoconfigure;

import com.newland.springboot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 自动配置导入选择
 * @author leell
 * @date 2023/5/13 21:42:18
 */
public class AutoConfigurationImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        ServiceLoader<AutoConfiguration> serviceLoader = ServiceLoader.load(AutoConfiguration.class);

        List<String> list = new ArrayList<>();
        for (AutoConfiguration configuration : serviceLoader) {
            list.add(configuration.getClass().getName());
        }
        return list.toArray(new String[0]);
    }
}
