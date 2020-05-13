package com.hyf.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * @author baB_hyf
 * @date 2020/05/12
 */
@Controller
@SessionAttributes({"name", "jsessionid", "JSESSIONID"})
public class SessionController {

	@RequestMapping("session")
	public String testSessionAttribute(@ModelAttribute("JSESSIONID") String JSESSIONID, ModelMap model, SessionStatus status) {
		System.out.println("JSESSIONID: " + JSESSIONID);
		if(JSESSIONID != null) {
			model.remove("name");
//			status.setComplete();
			return "success";
		}
		return "redirect:/session";
	}

	@ModelAttribute
	public void setSessionAttribute(Model model) {
		model.addAttribute("name", "张三");
		model.addAttribute("JSESSIONID", "ajsdfiawjepfoawef");
		model.addAttribute("flag", false);
	}
}
