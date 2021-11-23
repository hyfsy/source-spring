package com.hyf.demo.aop.example;

import com.hyf.demo.aop.common.UserService;
import com.hyf.demo.aop.common.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy // (exposeProxy = true)
@ComponentScan("com.hyf.demo.aop.example")
public class T {

	@Bean
	public static UserService userService() {
		return new UserServiceImpl();
	}

	public static void main(String[] args) {

		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(T.class)) {

			UserService userService = ctx.getBean(UserService.class);
			userService.update(1);

			// SupermanService supermanService = (SupermanService) userService;
			// supermanService.fly();

			// LoginService loginService = ctx.getBean(LoginService.class);
			// loginService.loginByEmail(1);
		}
	}
}
