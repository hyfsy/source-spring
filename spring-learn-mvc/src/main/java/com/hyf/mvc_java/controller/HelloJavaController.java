package com.hyf.mvc_java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author baB_hyf
 * @date 2020/05/09
 */
@Controller
public class HelloJavaController {

	@RequestMapping("helloJava")
	public String testHelloJava() {
		return "success";
	}
}
