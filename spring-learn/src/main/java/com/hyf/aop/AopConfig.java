package com.hyf.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author baB_hyf
 * @date 2020/02/21
 */
@Configuration
@ComponentScan("com.hyf.aop.other")
@EnableAspectJAutoProxy(exposeProxy = true)
public class AopConfig {

	public AopConfig(){
		System.out.println("-----------------------------------------------------");
	}

}
