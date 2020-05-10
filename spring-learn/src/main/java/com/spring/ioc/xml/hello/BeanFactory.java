package com.spring.ioc.xml.hello;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 简单工厂创建Bean对象
 */
public class BeanFactory {

    private static Properties prop = new Properties();
    // 存放Bean对象的容器
    private static Map<String, Object> beans = new HashMap<>();

    static {
        try {
            // 准确获取资源文件
            prop.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));

            Enumeration<Object> keys = prop.keys();
            while(keys.hasMoreElements()){
                String key = keys.nextElement().toString();
                String clazz = prop.getProperty(key);
                Object o = Class.forName(clazz).getConstructor().newInstance();
                beans.put(key, o);
            }
        } catch (Exception e) {
            // 对象初始化错误
            e.printStackTrace();
            throw new ExceptionInInitializerError();
        }

    }

    /**
     * 从容器中获取Bean对象
     */
    public static Object getBean(String beanName){
        return beans.get(beanName);
    }
}
