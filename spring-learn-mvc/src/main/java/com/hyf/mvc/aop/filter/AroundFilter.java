package com.hyf.mvc.aop.filter;

import org.apache.tools.ant.types.FilterChain;

import java.io.IOException;

public class AroundFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			System.out.println("around before");
			chain.doFilter(request, response);
			System.out.println("around after returning");
		} catch (Throwable t) {
			System.out.println("around after throwing");
		} finally {
			System.out.println("around after");
		}
	}
}
