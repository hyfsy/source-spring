package com.hyf.ioc.other.config;

import com.hyf.ioc.other.event.MyListener;
import org.springframework.context.annotation.Bean;

/**
 * @author baB_hyf
 * @date 2020/03/20
 */
public interface SpringConfigI {

	@Bean
	default MyListener myListener() {
		return new MyListener();
	}
}
