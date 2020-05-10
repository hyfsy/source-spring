package com.spring.ioc.xml.hello.dao.impl;

import com.spring.ioc.xml.hello.dao.IAccountDao;

public class AccountDaoImpl implements IAccountDao {

    @Override
    public void getAllAccount() {
        System.out.println("获取账户信息成功");
    }
}
