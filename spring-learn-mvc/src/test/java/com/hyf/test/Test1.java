package com.hyf.test;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author baB_hyf
 * @date 2020/04/12
 */
public class Test1 extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[0];
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[0];
	}

	@Override
	protected String[] getServletMappings() {
		return new String[0];
	}
}
