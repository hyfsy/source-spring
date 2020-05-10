package com.hyf.ioc.other.parent;

/**
 * @author baB_hyf
 * @date 2020/03/18
 */
//@Configuration
public class ParentConfig {

	public MyParent parent(){
		return new MyParent() {
		};
	}

	public MyChild child(){
		return new MyChild();
	}
}
