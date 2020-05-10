package com.hyf.ioc.other.parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 和继承没有关系，只要子bean定义中有父bean模板定义的属性即可
 *
 * @author baB_hyf
 * @date 2020/03/19
 */
@Component
public class MyChild extends MyParent {

	private Integer id;

	private Integer age;

	@Autowired
	private GrandSon son;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public GrandSon getSon() {
		return son;
	}

	public void setSon(GrandSon son) {
		this.son = son;
	}

	@Override
	public String toString() {
		return "MyChild{" +
				"id=" + id +
				", name=" + this.getName() +
				", age=" + age +
				", son=" + son +
				'}';
	}

	@Component
	class GrandSon {
		public Integer weight;

		public Integer getWeight() {
			return weight;
		}

		public void setWeight(Integer weight) {
			this.weight = weight;
		}

		@Override
		public String toString() {
			return "GrandSon{" +
					"weight=" + weight +
					'}';
		}
	}
}
