
package com.hyf.tx.other.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * 处理 @Service 的 bean名称，去除其尾部的 Impl
 *
 * @author baB_hyf
 * @date 2020/04/12
 */
public class ServiceNameGenerator extends DefaultBeanNameGenerator {

	private static final String SERVICE_IMPL_SUFFIX = "Impl";

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {

		if(definition.getBeanClassName() == null) {
			return super.generateBeanName(definition, registry);
		}

		String defaultName = ClassUtils.getShortName(definition.getBeanClassName());
		if (defaultName.endsWith(SERVICE_IMPL_SUFFIX)) {
			defaultName = defaultName.substring(0, defaultName.length() - SERVICE_IMPL_SUFFIX.length());
		}
		return Introspector.decapitalize(defaultName);
	}
}
