package framework.context;

import framework.annotation.Autowired;
import framework.annotation.Component;
import framework.xml.XMLBeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private Map<String, Object> beans = new HashMap<>();
    private XMLBeanFactory xmlBeanFactory;

    public ApplicationContext(String xmlConfigPath) {
        xmlBeanFactory = new XMLBeanFactory(xmlConfigPath);
        initializeBeans();
    }

    private void initializeBeans() {
        // First, create all beans from XML configuration
        beans.putAll(xmlBeanFactory.getBeans());
        
        // Then process annotations
        processAnnotations();
    }

    private void processAnnotations() {
        for (Object bean : beans.values()) {
            Class<?> clazz = bean.getClass();
            
            // Process field injection
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    injectField(bean, field);
                }
            }
            
            // Process constructor injection
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (constructor.isAnnotationPresent(Autowired.class)) {
                    injectConstructor(bean, constructor);
                }
            }
            
            // Process setter injection
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Autowired.class)) {
                    injectSetter(bean, method);
                }
            }
        }
    }

    private void injectField(Object bean, Field field) {
        try {
            field.setAccessible(true);
            String beanName = field.getAnnotation(Autowired.class).value();
            if (beanName.isEmpty()) {
                beanName = field.getType().getSimpleName();
            }
            Object dependency = beans.get(beanName);
            if (dependency != null) {
                field.set(bean, dependency);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error injecting field: " + field.getName(), e);
        }
    }

    private void injectConstructor(Object bean, Constructor<?> constructor) {
        try {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] args = new Object[paramTypes.length];
            
            for (int i = 0; i < paramTypes.length; i++) {
                String beanName = paramTypes[i].getSimpleName();
                args[i] = beans.get(beanName);
            }
            
            constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Error injecting constructor", e);
        }
    }

    private void injectSetter(Object bean, Method method) {
        try {
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1) {
                throw new RuntimeException("Setter method must have exactly one parameter");
            }
            
            String beanName = method.getAnnotation(Autowired.class).value();
            if (beanName.isEmpty()) {
                beanName = paramTypes[0].getSimpleName();
            }
            
            Object dependency = beans.get(beanName);
            if (dependency != null) {
                method.invoke(bean, dependency);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error injecting setter: " + method.getName(), e);
        }
    }

    public <T> T getBean(String name, Class<T> type) {
        Object bean = beans.get(name);
        if (bean == null) {
            throw new RuntimeException("Bean not found: " + name);
        }
        return type.cast(bean);
    }

    public <T> T getBean(Class<T> type) {
        return getBean(type.getSimpleName(), type);
    }
} 