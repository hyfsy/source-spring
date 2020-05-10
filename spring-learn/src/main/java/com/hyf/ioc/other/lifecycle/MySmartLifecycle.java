package com.hyf.ioc.other.lifecycle;

import org.springframework.context.SmartLifecycle;

/**
 * @author baB_hyf
 * @date 2020/03/16
 */
//@Component
public class MySmartLifecycle implements SmartLifecycle {

	private boolean isRunning = false;

	@Override
	public void start() {
		System.out.println("my smart lifecycle start");
		isRunning = true;
	}

	@Override
	public void stop() {
		System.out.println("my smart lifecycle stop");
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
}
