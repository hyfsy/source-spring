package com.hyf.mvc_annotation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author baB_hyf
 * @date 2020/05/09
 */
@Controller
public class HelloAnnoController {

	@RequestMapping("helloAnnotation")
	public String testHelloAnnotation() {
		return "success";
	}
}
