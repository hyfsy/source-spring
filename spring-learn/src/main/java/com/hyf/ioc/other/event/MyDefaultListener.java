package com.hyf.ioc.other.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author baB_hyf
 * @date 2020/03/17
 */
//@Component
public class MyDefaultListener implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("ContextRefreshedEvent监听器发布事件：" + event);
	}
}
