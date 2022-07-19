package com.hyf.mvc.aop.filter;

import javax.servlet.*;
import java.io.IOException;

public class AfterReturningFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
		System.out.println("after returning");
	}
}
