package com.hyf.message;

import com.hyf.config.AppConfig;
import com.hyf.message.order.OrderManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author baB_hyf
 * @date 2020/10/17
 */
public class Test {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		OrderManager bean = context.getBean(OrderManager.class);
		System.out.println(bean);
		// bean.placeOrder(null);
		// bean.placeOrder2(null);
		bean.placeOrder3(null);

	}
}
