package com.hyf.valid;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author baB_hyf
 * @date 2020/05/12
 */
@Configuration
@ComponentScan("com.hyf.valid.other.service")
@EnableAspectJAutoProxy
public class ValidConfig {

	/**
	 * 防止与 SpringMVC自动创建的 Optional校验工厂bean冲突
	 */
//	@Primary
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		factoryBean.setProviderClass(HibernateValidator.class);
//		factoryBean.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
//		factoryBean.setValidationMessageSource(resourceBundleMessageSource());
		return factoryBean;
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		MethodValidationPostProcessor validationPostProcessor = new MethodValidationPostProcessor();
		validationPostProcessor.setValidator(localValidatorFactoryBean().getValidator());
		return validationPostProcessor;
	}

//	@Bean
//	public ReloadableResourceBundleMessageSource resourceBundleMessageSource() {
//		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//		messageSource.setBasenames("jdbc");
//		return messageSource;
//	}
}
