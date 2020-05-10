package com.hyf.aop.other.log;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2020/03/30
 */
@Aspect
@Component
//@Configuration // 可行
public class AopLogger {

	@Pointcut("execution(* com.hyf.aop.other.service..*(..))")
	public void pc1() {
	}

	@Pointcut(value = "execution(* com.hyf.aop.other.service..*(..)) && args(str)", argNames = "str")
	public void pc2(String str) {
	}

	@Before("pc1()")
	public void before() {
		System.out.println("---before");
	}

	@AfterReturning(value = "pc1()", returning = "rtn")
	public void afterReturning(Object rtn) {
		System.out.println("---afterReturning: " + rtn);
	}

	@AfterThrowing(value = "pc1()", throwing = "ex")
	public void afterThrowing(Throwable ex) {
		System.out.println("---afterThrowing: " + ex);
	}

	@After("pc1()")
	public void after() {
		System.out.println("---after");
	}


	@Around(value = "pc1()")
	public Object around(ProceedingJoinPoint pjp) {
		Object rtnValue = null;
		try {
			System.out.println("---around invoke before");
			rtnValue = pjp.proceed();
			System.out.println("---around invoke afterReturning: " + rtnValue);
		} catch (Throwable t) {
			System.out.println("---around invoke afterThrowing: " + t);
		} finally {
			System.out.println("---around invoke after");
		}
		return rtnValue;
	}









	// 参数匹配设置，底层通过索引而非参数名称
//	@Around(value = "pc2(str)", argNames = "pjp,str")
//	public Object around(ProceedingJoinPoint pjp, String str) {
//		return null;
//	}

	// 方法签名排序，AspectJ内部排序规则
//	@Around("pc2(str)")
//	public Object around(Object str) {
//		return null;
//	}
}
