package com.hyf.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author baB_hyf
 * @date 2020/04/12
 */
@Controller
@RequestMapping("helloC")
public class HelloController {

	@RequestMapping("hello")
	public String hello() {
		System.out.println("hello");
		return "success";
	}

	@RequestMapping("hello2/{aaa}/a")
	public String hello2(@PathVariable String aaa,
						 @MatrixVariable(pathVar = "aaa", name = "a") String a) {
		System.out.println("variable: " + aaa);
		System.out.println("matrix: " + a);

		// path.charAt(i) > ' ' && path.charAt(i) != 127

		return "success";
	}

	@RequestMapping("requestparam")
	public String hello4(@RequestParam String str) {
		System.out.println("requestParam: " + str);
		return "success";
	}

	@RequestMapping("error")
	public String hello3(@Validated String str, BindingResult result) {
		System.out.println("validated: " + str);
		System.out.println("bindingResult: " + result);
		return "success";
	}
}
