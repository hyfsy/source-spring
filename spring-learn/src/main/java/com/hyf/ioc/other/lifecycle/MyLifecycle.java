package com.hyf.ioc.other.lifecycle;

import org.springframework.context.Lifecycle;

/**
 * @author baB_hyf
 * @date 2020/03/1
 */
//@Component
public class MyLifecycle implements Lifecycle {

	@Override
	public void start() {
		System.out.println("my lifecycle start");
	}

	@Override
	public void stop() {
		System.out.println("my lifecycle stop");
	}

	@Override
	public boolean isRunning() {
		return true;
	}
}
