package com.hyf.ioc.other.common;

import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * test JSR-250 annotation
 *
 * @author baB_hyf
 * @date 2020/03/28
 */
@Component
public class ResourceLazy {

	@Resource(name = "myLifecycle")
	@Lazy
	public Lifecycle lifecycle;

	public void printResource(){
		System.out.println(lifecycle);
	}



}
