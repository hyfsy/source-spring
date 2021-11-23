package com.hyf.demo.aop.common;

public class UserServiceImpl implements UserService {

	@Override
	public boolean update(int id) {
		System.out.println("update success: " + id);
		return true;
	}
}
