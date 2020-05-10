package com.hyf.ioc;

import com.hyf.ioc.other.config.AbstractSpringConfig;
import com.hyf.ioc.other.config.SpringConfigI;
import org.springframework.context.annotation.*;

/**
 * @author baB_hyf
 * @date 2020/02/21
 * @see this:{@link ConfigurationClassParser#doProcessConfigurationClass}
 */
@Configuration
@ComponentScans(value = {
//		@ComponentScan("com.hyf.ioc.other.lifecycle"),
//		@ComponentScan("com.hyf.ioc.other.event"),
//		@ComponentScan("com.hyf.ioc.other.factory"),
//		@ComponentScan("com.hyf.ioc.other.parent"),
//		@ComponentScan("com.hyf.ioc.other.nest"),
//		@ComponentScan("com.hyf.ioc.other.common"),
		@ComponentScan("com.hyf.ioc.other.autowire"),
})
//@Import({JdbcConfig.class, EventConfig.class}) // @Import 导入的类的bean名称默认为【全路径】
@ImportResource("spring-ioc.xml")
@PropertySource("jdbc.properties")
public class SpringConfig extends AbstractSpringConfig implements SpringConfigI {

	public SpringConfig(){
		System.out.println(1);
	}

}
