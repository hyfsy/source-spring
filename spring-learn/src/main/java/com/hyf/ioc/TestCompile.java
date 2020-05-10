package com.hyf.ioc;

import com.hyf.ioc.other.autowire.AutowireAnnotation;
import com.hyf.ioc.other.event.EventService;
import com.spring.ioc.xml.ioc.Account;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author baB_hyf
 * @date 2020/02/21
 */
public class TestCompile {

	@Test
	public void testXmlHello() {
		ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");

//		Object myChild = ioc.getBean("7");
//		System.out.println(myChild);

		Account account = (Account)ioc.getBean("account");
		System.out.println(account);

		// 需要手动销毁才会执行destroy方法
		((ClassPathXmlApplicationContext) ioc).close();

	}

	@Test
	public void testAnnotationHello() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		SpringConfig bean = context.getBean(SpringConfig.class);
		System.out.println(bean);



		// 测试 @Autowired 注入、候选构造获取
		AutowireAnnotation autowireAnnotation = context.getBean(AutowireAnnotation.class);
		System.out.println(autowireAnnotation);



		// 测试 JSR-250 相关注解
//		ResourceLazy common = context.getBean(ResourceLazy.class);
//		common.printResource();
//		Object myLifecycle = context.getDefaultListableBeanFactory().getSingleton("myLifecycle");
//		Object target = ((Advised) common.lifecycle).getTargetSource().getTarget();
//		System.out.println("懒加载对象是否相等：" + myLifecycle.equals(target));



		// 测试嵌套集合
//		NestCollection nestCollection = context.getBean(NestCollection.class);
//		System.out.println(nestCollection);
//		System.out.println("list数量：" + nestCollection.getNestList().size());
//		System.out.println("map数量：" + nestCollection.getNestMap().size());



		// 测试嵌套属性
//		NestIII nestIII = context.getBean(NestIII.class);
//		System.out.println(nestIII);



		// 测试循环别名引用
//		Object bean1 = context.getBean("6");
//		System.out.println(bean1);



		// 测试自定义事件发布
//		publishEvent(context);

		context.close();
	}

	/**
	 * 自定义事件发布测试
	 */
	public void publishEvent(ApplicationContext context){
		EventService eventService = context.getBean("eventService", EventService.class);
		eventService.publishMyEvent();
	}
}
