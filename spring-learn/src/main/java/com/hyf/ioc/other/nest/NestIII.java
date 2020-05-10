package com.hyf.ioc.other.nest;

/**
 * @author baB_hyf
 * @date 2020/03/27
 */
public class NestIII {

	private NestII nestII;

	private Integer age;

	public NestII getNestII() {
		return nestII;
	}

	public void setNestII(NestII nestII) {
		this.nestII = nestII;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "NestIII{" +
				"nestII=" + nestII +
				", age=" + age +
				'}';
	}
}
