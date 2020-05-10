package com.hyf.ioc.other.event;

import org.springframework.context.ApplicationListener;

/**
 * Listener只是处理对应的Event对象
 * 
 * @author baB_hyf
 * @date 2020/03/16
 */
//@Component
public class MyListener implements ApplicationListener<MyEvent>
{

    @Override
    public void onApplicationEvent(MyEvent event) {
        System.out.println("自定义事件发布：" + event.getSource());
    }

}
