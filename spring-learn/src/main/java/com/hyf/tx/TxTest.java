package com.hyf.tx;

import com.hyf.tx.other.service.MoneyServiceI;
import com.hyf.tx.other.service.PropagationService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author baB_hyf
 * @date 2020/03/30
 */
public class TxTest {

	@Test
	public void testTxXmlHello() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-tx.xml");

		MoneyServiceI bean = context.getBean(MoneyServiceI.class);
		System.out.println(bean.getClass());
		bean.transfer("a", "b");

	}


	@Test
	public void testTxAnnotationHello(){
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TxConfig.class);

		MoneyServiceI bean = context.getBean(MoneyServiceI.class);
		System.out.println(bean.getClass());
		bean.transfer("a", "b");
	}


	/**
	 * 测试事务传播行为
	 */
	@Test
	public void testPropagation() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TxConfig.class);

		PropagationService bean = context.getBean(PropagationService.class);
		System.out.println(bean.getClass());
		bean.testSupports();
	}

	/**
	 * 测试xml与注解的混合事务
	 * 需注释Config内的 @Bean配置
	 */
	@Test
	public void testMixtureTransaction() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-tx.xml");

		MoneyServiceI bean = context.getBean(MoneyServiceI.class);
		System.out.println(bean.getClass());
//		bean.transfer("a", "b");
//		bean.insertMoney(new Money(10000));
	}


}
