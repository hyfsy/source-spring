package com.hyf.ioc.other.autowire;

import com.hyf.ioc.other.autowire.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AutowiredAnnotationBeanPostProcessor 查找候选的构造函数：
 *
 *
 * 从 当前类 及其 父类 中寻找 @Autowired/@Value 注解注释的构造函数
 *
 * 单个构造函数：
 * - 只有一个无注解默认构造，返回 null
 * - 只有一个有注解默认构造，返回 默认构造
 * - 只有一个无注解有参构造，返回 唯一的候选
 *
 * 多个构造函数（无注解）：返回 null
 *
 * 多个构造函数（有注解）：
 * - 只要有一个注解的 required 为 true -> 抛异常
 * - 所有注解的 required 都为 false -> 返回所有注解注释的构造 （最后创建时会 从上往下选择最后一个构造函数）
 * - 存在（注解可有可无）默认构造会一起合并返回，条件和上方一样
 *
 * PS: 忽略了kotlin相关
 *
 * @author baB_hyf
 * @date 2020/03/29
 */
@Component
public class AutowireAnnotation {

	@Autowired
	private static Computer computer;

	private Mobile mobile;

	private Mouse mouse;

	private IPhone iPhone;

	private Screen screen;

//	@Autowired(required = false)
	public AutowireAnnotation(){
	}

//	public AutowireAnnotation(Mobile mobile){
//		this.mobile = mobile;
//	}

//	public AutowireAnnotation(@Autowired Mouse mouse){
//		this.mouse = mouse;
//	}

	@Autowired(required = false)
	public AutowireAnnotation(Computer computer){
		this.computer = computer;
	}

	@Autowired(required = false)
	public AutowireAnnotation(IPhone iPhone){
		this.iPhone = iPhone;
	}

//	@Autowired
//	public void setScreen(Screen screen, Mouse mouse) {
//		this.screen = screen;
//		this.mouse = mouse;
//	}

	@Autowired(required = false)
	public AutowireAnnotation(Computer computer, IPhone iPhone){
//		this.computer = computer;
		this.iPhone = iPhone;
	}


	// 调试 descriptor.getDependencyType(); 在多个参数的情况下通过索引获取类型
//	@Autowired
//	public void customName(Screen screen, Mouse mouse) {
//		this.screen = screen;
//		this.mouse = mouse;
//	}

	@Override
	public String toString() {
		return "AutowireAnnotation:" +
				"\n--computer=" + computer +
				"\n--mobile=" + mobile +
				"\n--mouse=" + mouse +
				"\n--iPhone=" + iPhone +
				"\n--screen=" + screen;
	}
}
