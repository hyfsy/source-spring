package com.hyf.ioc.other.parent;

import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2020/03/19
 */
@Component
public abstract class MyParent {

	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MyParent{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
