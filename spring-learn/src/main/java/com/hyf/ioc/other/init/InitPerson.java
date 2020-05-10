package com.hyf.ioc.other.init;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author baB_hyf
 * @date 2020/03/25
 */
public class InitPerson implements InitializingBean, DisposableBean {

	@PostConstruct
	public void postConstruct() {
		System.out.println("@PostConstruct invoke...");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("interface - afterPropertiesSet invoke...");
	}

	public void init() {
		System.out.println("init-method invoke...");
	}


	//-------------------------------------------------


	@PreDestroy
	public void preDestroy() {
		System.out.println("preDestroy invoke...");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("interface - destroy invoke...");
	}

	/**
	 * 只能有一个boolean类型的参数，默认true
	 * 只支持boolean类型，不支持包装类型
	 *
	 * @see #{@link DisposableBeanAdapter#invokeCustomDestroyMethod}
	 * @param whether
	 */
	public void destroyMethod(boolean whether) {
		System.out.println(whether); // default true
		System.out.println("destroy-method invoke...");
	}

}
