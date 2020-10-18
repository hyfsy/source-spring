package com.hyf.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author baB_hyf
 * @date 2020/10/18
 */
@Configuration
public class MessageConfiguration {

	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl(); // 这边只有实现类有set方法
		javaMailSender.setProtocol("smtp"); // 使用SMTP协议（简单邮件传输协议）
		javaMailSender.setHost("smtp.qq.com"); // qq邮箱SMTP服务器地址
		javaMailSender.setPort(465); // qq邮箱SMTP服务器端口

		javaMailSender.setUsername("1577975140@qq.com");
		javaMailSender.setPassword("hsfdpduwhzcnfjfe");

		// 以下几个属性最好设置一下，因为 465端口实现了stmps协议
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.setProperty("mail.smtp.socketFactory.fallback", "false");
		prop.setProperty("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.auth", "true");// 需要经过授权
		prop.put("mail.smtp.timeout", "25000");
		prop.put("mail.smtp.ssl.enable", "true");// 使用SSL安全连接
		prop.put("mail.debug", "true");// 控制台显示debug信息

		javaMailSender.setJavaMailProperties(prop);

		return javaMailSender;
	}

	@Bean
	public SimpleMailMessage simpleMailMessage() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("1577975140@qq.com");

		return simpleMailMessage;
	}
}
