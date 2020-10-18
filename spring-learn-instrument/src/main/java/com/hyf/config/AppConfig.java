package com.hyf.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author baB_hyf
 * @date 2020/10/17
 */
@Configuration
@ComponentScan({
		"com.hyf.message",
		"com.hyf.cache",
		"com.hyf.jms"
})
@ImportResource("classpath:app.xml")
public class AppConfig {
}
