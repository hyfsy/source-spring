package com.hyf.ioc.other.config;

import com.hyf.ioc.other.lifecycle.MyLifecycle;
import org.springframework.context.annotation.Bean;

/**
 * @author baB_hyf
 * @date 2020/03/20
 */
public class AbstractSpringConfig {

	@Bean
	public MyLifecycle myLifecycle(){
		return new MyLifecycle();
	}

}
