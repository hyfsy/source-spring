package com.hyf.aop.other.service.impl;

import com.hyf.aop.other.service.ParentServiceI;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2020/04/04
 */
@Component("parentService")
public class ParentServiceImpl implements ParentServiceI {

	@Override
	public void changePrint() {
		System.out.println("I'm changed");
	}

}
