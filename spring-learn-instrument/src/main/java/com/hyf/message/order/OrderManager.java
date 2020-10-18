package com.hyf.message.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author baB_hyf
 * @date 2020/10/17
 */
@Component
public class OrderManager implements IOrderManager {

	@Autowired
	private MailSender mailSender;

	@Autowired
	private SimpleMailMessage simpleMailMessage;

	@Override
	public void placeOrder(Map<String, Object> order) {
		System.out.println(mailSender);
		System.out.println(simpleMailMessage);

		simpleMailMessage.setTo("3072264584@qq.com");
		simpleMailMessage.setText("hello");

		try {
			mailSender.send(simpleMailMessage);
		} catch (MailException e) {
			// simply log it and go on...
			System.err.println(e.getMessage());
		}

	}
}
