package com.hyf.test;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * @author baB_hyf
 * @date 2020/05/10
 */
public class Test5 extends AbstractDispatcherServletInitializer {
	@Override
	protected WebApplicationContext createServletApplicationContext() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[0];
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		return null;
	}
}
