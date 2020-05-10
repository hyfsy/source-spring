package com.hyf.tx;

import com.alibaba.druid.pool.DruidDataSource;
import com.hyf.tx.other.aspect.AopAspect;
import com.hyf.tx.other.util.ServiceNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * @author baB_hyf
 * @date 2020/04/05
 */
@Configuration
@ComponentScan(basePackages = "com.hyf.tx.other.service", nameGenerator = ServiceNameGenerator.class) // , nameGenerator = ServiceNameGenerator.class
@PropertySource("jdbc.properties")
@EnableTransactionManagement
// aop测试事务拦截器链顺序
//@Import(AopAspect.class)
//@EnableAspectJAutoProxy
public class TxConfig {

	public TxConfig(){
		System.out.println("-----------------------------------------------------");
	}

	/**
	 * 事务的事件发布相关
	 */
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void pubListener(ApplicationEvent event) {
		System.out.println(event);
	}

	/**
	 * 编程式事务
	 */
	@Bean
	public TransactionTemplate transactionTemplate() {
		return new TransactionTemplate(platformTransactionManager());
	}

	/**
	 * 声明式事务相关
	 */
	@Bean
	public PlatformTransactionManager platformTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public DataSource dataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(driver);
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(userName);
		druidDataSource.setPassword(password);
		return druidDataSource;
	}

	@Value("${driver}")
	private String driver;

	@Value("${url}")
	private String url;

	@Value("${jdbc.username}") // 不能直接username,被spring征用了
	private String userName;

	@Value("${password}")
	private String password;

}
