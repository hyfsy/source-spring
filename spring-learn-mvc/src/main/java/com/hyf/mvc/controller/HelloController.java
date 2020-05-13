package com.hyf.mvc.controller;

import com.hyf.mvc.pojo.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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

	/**
	 * 注意：文件上传请求为post,返回字符串会默认请求转发到静态资源，导致405  原本 post -> 静态资源 get/head
	 * 所以在返回时需要使用重定向
	 */
	@RequestMapping("file")
	public String file(MultipartFile multipartFile, String testText) {
		System.out.println("MultipartFile: " + multipartFile);
		System.out.println("testText: " + testText);
//		return "success";
		return "redirect:/pages/success.html";
	}

	@RequestMapping("error")
	public String hello3(@Valid Person person, BindingResult result) {
		System.out.println("validated: " + person);
		System.out.println("错误数量：" + result.getErrorCount());
		System.out.println("bindingResult: " + result);
		return "success";
	}

	@ModelAttribute
	public void initPojo(Model model) {
		model.addAttribute("person", new Person(1, "zs", 20));
	}
}
