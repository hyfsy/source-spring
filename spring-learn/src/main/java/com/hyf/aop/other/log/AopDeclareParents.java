package com.hyf.aop.other.log;


import com.hyf.aop.other.service.ParentServiceI;
import com.hyf.aop.other.service.impl.ParentServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2020/04/04
 */
@Aspect
@Component
public class AopDeclareParents {

	@Pointcut("execution(* com.hyf.aop.other.service.impl..*(..))")
	public void pcParent() {
	}

	@Pointcut(value = "bean(transferService)")
	public void pcBean() {
	}

	/**
	 * 类过滤，切入点表达式
	 */
	@DeclareParents(value = "com.hyf.aop.other.service..*", defaultImpl = ParentServiceImpl.class)
	ParentServiceI parentService;


	@Before("com.hyf.aop.other.log.AopLogger.pc1()")
	public void beforeParent() {
		System.out.println("---beforeParent");
	}


}
