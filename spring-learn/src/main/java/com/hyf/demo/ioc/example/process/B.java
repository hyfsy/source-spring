package com.hyf.demo.ioc.example.process;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2021/11/08
 */
@Component
public class B implements InstantiationAwareBeanPostProcessor {

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		if ("z".equals(beanName))
			System.out.println(beanName + " postProcessBeforeInstantiation");
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		if ("z".equals(beanName))
			System.out.println(beanName + " postProcessAfterInstantiation");
		return false;
	}
}
