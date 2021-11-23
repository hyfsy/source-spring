package com.hyf.demo.ioc.example.process;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2021/11/08
 */
@Component
public class A implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if ("z".equals(beanName))
			System.out.println(beanName + " postProcessBeforeInitialization");
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if ("z".equals(beanName))
			System.out.println(beanName + " postProcessAfterInitialization");
		return bean;
	}
}
