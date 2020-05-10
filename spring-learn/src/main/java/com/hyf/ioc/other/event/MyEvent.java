package com.hyf.ioc.other.event;

import org.springframework.context.ApplicationEvent;

/**
 * 事件需要注册到组播器中，组播器再寻找对应的Listener执行事件操作
 * 
 * @author baB_hyf
 * @date 2020/03/16
 */
public class MyEvent extends ApplicationEvent
{

    /**
     * Create a new ApplicationEvent.
     *
     * @param source
     *            the object on which the event initially occurred (never {@code null})
     */
    public MyEvent(Object source) {
        super(source);
    }

}
