package com.hyf.demo.aop.proxy.dynamic;

import com.hyf.demo.aop.common.UserService;
import com.hyf.demo.aop.common.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DP {

	public static void main(String[] args) {

		UserService userService = new UserServiceImpl();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		InvocationHandler invocationHandler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				if ("update".equals(method.getName())) {
					if ((int) args[0] == 1) {
						return method.invoke(userService, args);
					}
					else {
						return false;
					}
				}

				return method.invoke(userService, args);
			}
		};

		UserService userServiceProxy = (UserService) Proxy.newProxyInstance(
				classLoader,                                              //
				new Class[]{UserService.class},                           //
				invocationHandler);                                       //

		System.out.println(userServiceProxy.getClass());
		userServiceProxy.update(2);
	}
}
