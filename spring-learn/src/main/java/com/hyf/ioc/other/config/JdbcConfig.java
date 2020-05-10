package com.hyf.ioc.other.config;

import com.hyf.ioc.other.event.MyEvent;
import org.springframework.context.annotation.Bean;

/**
 * @author baB_hyf
 * @date 2020/03/20
 */
public class JdbcConfig {

	@Bean
	public MyEvent myEvent() {
		return new MyEvent("this");
	}

}
