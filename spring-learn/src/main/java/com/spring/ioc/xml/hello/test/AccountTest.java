package com.spring.ioc.xml.hello.test;

import com.spring.ioc.xml.hello.BeanFactory;
import com.spring.ioc.xml.hello.service.IAccountService;

public class AccountTest
{

    public static void  main(String[] args) {

        // 由我们手动创建对象
        // IAccountService accountService = new AccountServiceImpl();
        // 将自己创建对象的权利交由框架帮助我们创建，转移了创建对象的控制权

        // 由容器帮我们创建对象
        IAccountService accountService = (IAccountService) BeanFactory.getBean("accountService");
        accountService.getAllAccount();
    }
}
