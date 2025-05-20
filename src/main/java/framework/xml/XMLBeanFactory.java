package framework.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XMLBeanFactory {
    private Map<String, Object> beans = new HashMap<>();
    private BeanConfig beanConfig;

    public XMLBeanFactory(String xmlConfigPath) {
        try {
            JAXBContext context = JAXBContext.newInstance(BeanConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            beanConfig = (BeanConfig) unmarshaller.unmarshal(new File(xmlConfigPath));
            initializeBeans();
        } catch (JAXBException e) {
            throw new RuntimeException("Error parsing XML configuration", e);
        }
    }

    private void initializeBeans() {
        for (BeanDefinition beanDef : beanConfig.getBeans()) {
            try {
                Class<?> clazz = Class.forName(beanDef.getClassName());
                Object instance = createBeanInstance(clazz, beanDef);
                beans.put(beanDef.getId(), instance);
            } catch (Exception e) {
                throw new RuntimeException("Error creating bean: " + beanDef.getId(), e);
            }
        }
    }

    private Object createBeanInstance(Class<?> clazz, BeanDefinition beanDef) throws Exception {
        if (beanDef.getConstructorArgs() != null && !beanDef.getConstructorArgs().isEmpty()) {
            // Constructor injection
            Class<?>[] paramTypes = new Class[beanDef.getConstructorArgs().size()];
            Object[] args = new Object[beanDef.getConstructorArgs().size()];
            
            for (int i = 0; i < beanDef.getConstructorArgs().size(); i++) {
                ConstructorArg arg = beanDef.getConstructorArgs().get(i);
                paramTypes[i] = Class.forName(arg.getType());
                args[i] = beans.get(arg.getRef());
            }
            
            return clazz.getConstructor(paramTypes).newInstance(args);
        } else {
            // Default constructor
            return clazz.getDeclaredConstructor().newInstance();
        }
    }

    public Map<String, Object> getBeans() {
        return beans;
    }
} 