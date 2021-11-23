package com.hyf.demo.ioc.example.process;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2021/11/09
 */
@Component
public class C implements DestructionAwareBeanPostProcessor {

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		if ("z".equals(beanName))
			System.out.println("postProcessBeforeDestruction");
	}
}
