package com.hyf.mvc.aop.config;

import com.hyf.mvc.aop.filter.*;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.*;
import java.util.EnumSet;

public class AopFilterRegistration implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		Filter[] filters = new Filter[] {
				new AroundFilter(),
				new BeforeFilter(),
				new AfterFilter(),
				new AfterReturningFilter(),
				new AfterThrowingFilter()
		};


		for (Filter filter : filters) {
			FilterRegistration.Dynamic config =
					servletContext.addFilter(filter.getClass().getName(), filter);
			config.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/aop/*");
		}
	}
}
