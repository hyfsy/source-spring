package com.hyf.demo.aop.example.invalid;

import org.springframework.stereotype.Component;

@Component
public class LoginService {

	// @Autowired
	// EmailService emailService;

	public void loginByEmail(int loginId) {
		System.out.println("login");

		// LoginService loginService = (LoginService) AopContext.currentProxy();
		// loginService.sendEmail(loginId);

		// System.out.println(this.getClass());
		// System.out.println(loginService.getClass());

		sendEmail(loginId);
		// emailService.sendEmail(loginId);
	}

	public void sendEmail(int loginId) {
		System.out.println("send email...");
	}
}
