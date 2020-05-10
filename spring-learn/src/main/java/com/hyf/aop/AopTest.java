package com.hyf.aop;

import com.hyf.aop.other.service.ParentServiceI;
import com.hyf.aop.other.service.TransferServiceI;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author baB_hyf
 * @date 2020/03/30
 */
public class AopTest {

	@Test
	public void testAopXmlHello () {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");

		TransferServiceI bean = context.getBean(TransferServiceI.class);
		System.out.println(bean.getClass());
		bean.transfer();

//		bean.transferWithArgs("test");
//		bean.transferWithEx();

//		ParentServiceI parentService = (ParentServiceI) bean;
//		parentService.changePrint();

	}

	@Test
	public void testAopAnnotationHello () {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);

		TransferServiceI bean = context.getBean(TransferServiceI.class);
		System.out.println(bean.getClass());
		bean.transfer();

//		bean.transferWithArgs("test");
//		bean.transferWithEx();

//		ParentServiceI parentService = (ParentServiceI) bean;
//		parentService.changePrint();
	}
}
