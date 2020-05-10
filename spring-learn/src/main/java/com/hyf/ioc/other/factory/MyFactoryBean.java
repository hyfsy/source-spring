package com.hyf.ioc.other.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2020/03/27
 */
@Component
public class MyFactoryBean implements FactoryBean<MyTeacher> {

	@Override
	public MyTeacher getObject() throws Exception {
		System.out.println("工厂bean获取实例..");
		return new MyTeacher();
	}

	@Override
	public Class<?> getObjectType() {
		return MyTeacher.class;
	}
}
