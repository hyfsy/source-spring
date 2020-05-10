package com.spring.ioc.xml.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args){

        ApplicationContext ioc = new ClassPathXmlApplicationContext("bean.xml");
        Account account = (Account)ioc.getBean("account");
        System.out.println(account);

        // 需要手动销毁才会执行destroy方法
        ((ClassPathXmlApplicationContext) ioc).close();
    }
}
