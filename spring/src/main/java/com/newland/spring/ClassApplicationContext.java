package com.newland.spring;

import com.newland.spring.annotation.Autowired;
import com.newland.spring.annotation.Component;
import com.newland.spring.annotation.ComponentScan;
import com.newland.spring.annotation.Scope;
import com.newland.spring.bean.BeanDefinition;
import com.newland.spring.factory.BeanNameAware;
import com.newland.spring.factory.BeanPostProcessor;
import com.newland.spring.factory.InitializingBean;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class上下文
 * @author leell
 */
public class ClassApplicationContext {

    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    /**
     * Spring启动流程
     * @param configClass 配置类
     */
    public ClassApplicationContext(Class configClass) {
        // 扫描类 得到 BeanDefinition
        scan(configClass);

        // 实例化非懒加载单例bean
        //   1. 实例化
        //   2. 属性填充
        //   3. Aware回调
        //   4. 初始化
        //   5. 添加到单例池
        instanceSingletonBean();

    }

    private void instanceSingletonBean() {
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

            if (beanDefinition.getScope().equals(ScopeEnum.singleton)) {
                Object bean = doCreateBean(beanName, beanDefinition);

                singletonObjects.put(beanName, bean);
            }
        }
    }

    /**
     *  基于BeanDefinition来创建bean
     * @param beanName 名称
     * @param beanDefinition 定义
     * @return
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();

        try {
            // 实例化
            Constructor declaredConstructor = beanClass.getDeclaredConstructor();
            Object instance = declaredConstructor.newInstance();

            // 填充属性
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String fieldName = field.getName();
                    Object bean = getBean(fieldName);

                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }

            // Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }

            for (BeanPostProcessor beanPostProcessor: beanPostProcessorList) {
                beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }

            return instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void scan(Class configClass) {
        // 扫描class，转化为BeanDefinition对象，最后添加到beanDefinitionMap中

        // 先得到包路径
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String packagePath = componentScanAnnotation.value();

        // 扫描包路径得到classList
        List<Class> classList = genBeanClasses(packagePath);
        // 遍历class得到BeanDefinition
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(Component.class)) {
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setBeanClass(clazz);

                // 要么Spring自动生成，要么从Component注解上获取
                Component component = (Component) clazz.getAnnotation(Component.class);
                String beanName = component.value();

                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                    try {
                        BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                        beanPostProcessorList.add(instance);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }

                // 解析scope
                if (clazz.isAnnotationPresent(Scope.class)) {
                    Scope scope = (Scope) clazz.getAnnotation(Scope.class);
                    String scopeValue = scope.value();
                    if (ScopeEnum.singleton.name().equals(scopeValue)) {
                        beanDefinition.setScope(ScopeEnum.singleton);
                    } else {
                        beanDefinition.setScope(ScopeEnum.prototype);
                    }
                } else {
                    beanDefinition.setScope(ScopeEnum.singleton);
                }

                beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }

    /**
     * 获取指定包路径下class
     * @param packagePath 包路径
     * @return
     */
    private List<Class> genBeanClasses(String packagePath) {
        List<Class> beanClasses = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        packagePath = packagePath.replace(".", "/");
        URL resource = classLoader.getResource(packagePath);
        File file = new File(resource.getFile());

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\", ".");
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        beanClasses.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return beanClasses;
    }

    public Object getBean(String beanName) {

        if (singletonObjects.containsKey(beanName)) {
            return singletonObjects.get(beanName);
        } else {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            return doCreateBean(beanName, beanDefinition);
        }
    }
}
