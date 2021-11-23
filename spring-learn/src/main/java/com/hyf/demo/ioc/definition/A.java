package com.hyf.demo.ioc.definition;

/**
 * @author baB_hyf
 * @date 2021/11/06
 */
public class A {

	B b;

	public void doSomething() {
		b.c.doSomething();
	}
}
