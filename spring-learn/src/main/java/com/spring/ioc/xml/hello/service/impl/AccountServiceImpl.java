package com.spring.ioc.xml.hello.service.impl;

import com.spring.ioc.xml.hello.BeanFactory;
import com.spring.ioc.xml.hello.dao.IAccountDao;
import com.spring.ioc.xml.hello.service.IAccountService;

public class AccountServiceImpl implements IAccountService {

    @Override
    public void getAllAccount() {

        // private IAccountDao accountDao = new AccountDaoImpl();

        IAccountDao accountDao = (IAccountDao) BeanFactory.getBean("accountDao");

        accountDao.getAllAccount();
    }

}
