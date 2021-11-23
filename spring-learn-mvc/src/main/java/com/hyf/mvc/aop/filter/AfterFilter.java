package com.hyf.mvc.aop.filter;

import org.apache.tools.ant.types.FilterChain;

import java.io.IOException;

public class AfterFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			System.out.println("after");
		}
	}
}
