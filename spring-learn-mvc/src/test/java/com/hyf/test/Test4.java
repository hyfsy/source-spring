package com.hyf.test;

import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author baB_hyf
 * @date 2020/05/10
 */
public class Test4 extends AbstractContextLoaderInitializer {

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		return null;
	}
}
