package com.hyf.ioc.other.nest;

/**
 * @author baB_hyf
 * @date 2020/03/27
 */
public class NestII {

	private NestI nestI;

	private String name;

	public NestI getNestI() {
		return nestI;
	}

	public void setNestI(NestI nestI) {
		this.nestI = nestI;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "NestII{" +
				"nestI=" + nestI +
				", name='" + name + '\'' +
				'}';
	}
}
