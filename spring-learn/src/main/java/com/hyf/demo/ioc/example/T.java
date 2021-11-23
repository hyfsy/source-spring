package com.hyf.demo.ioc.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.hyf.demo.ioc.example")
public class T {

	@Autowired
	private X x;

	@Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
	public Z z() {
		return new Z();
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(T.class);
		System.out.println("---------------------------");
		ctx.close();

		// System.out.println(ctx.getBean("x"));
	}
}
