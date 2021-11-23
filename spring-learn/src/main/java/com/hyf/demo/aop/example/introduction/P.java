package com.hyf.demo.aop.example.introduction;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class P {

	public static final String PC1 = "com.hyf.demo.aop.common.UserService+";


	@DeclareParents(value = PC1, defaultImpl = SupermanServiceImpl.class)
	public SupermanService supermanService;

}
