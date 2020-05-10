package com.hyf.ioc.other.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 默认按照声明顺序执行
 *
 * @author baB_hyf
 * @date 2020/03/27
 */
@Configuration
public class EventConfig {

	@Order(1)
	@EventListener
	public void call_I(ContextRefreshedEvent e1) {
		System.out.println("接受 refresh 时才调用：");
		System.out.println(e1);
	}

	@Order(3)
	@EventListener({ContextRefreshedEvent.class, ContextClosedEvent.class})
	public void call_II() {
		System.out.println("接受 refresh close 时调用：");
	}

	@Order(2)
	@Async
	@EventListener(ContextClosedEvent.class)
	public void call_III() {
		System.out.println("接受 close 时才调用：");
		System.out.println(this);
	}

}
