package com.hyf.demo.ioc.definition;

public class O {


	public static void main(String[] args) {

		// 控制反转 依赖注入

		// 谁控制谁
		// 反转了什么
		// 为什么要反转

		C c = new C();

		B b = new B();
		b.c = c;

		A a = new A();
		a.b = b;

		// new  ->  工厂模式  ->   容器 (反射+工厂)


		a.doSomething();
	}
}
