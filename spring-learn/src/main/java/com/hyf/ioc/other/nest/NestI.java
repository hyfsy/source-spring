package com.hyf.ioc.other.nest;

/**
 * @author baB_hyf
 * @date 2020/03/27
 */
public class NestI {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "NestI{" +
				"id=" + id +
				'}';
	}
}
