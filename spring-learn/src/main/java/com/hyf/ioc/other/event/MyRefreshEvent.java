package com.hyf.ioc.other.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author baB_hyf
 * @date 2020/03/17
 */
public class MyRefreshEvent extends ContextRefreshedEvent {
	/**
	 * Create a new ContextRefreshedEvent.
	 *
	 * @param source the {@code ApplicationContext} that has been initialized
	 *               or refreshed (must not be {@code null})
	 */
	public MyRefreshEvent(ApplicationContext source) {
		super(source);
	}
}
