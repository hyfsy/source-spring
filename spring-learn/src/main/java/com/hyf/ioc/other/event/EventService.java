package com.hyf.ioc.other.event;

import com.sun.istack.internal.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.SimpleApplicationEventMulticaster;


/**
 * @author baB_hyf
 * @date 2020/03/16
 */
//@Component
public class EventService implements ApplicationEventPublisherAware
{

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(@NotNull ApplicationEventPublisher applicationEventPublisher) {

		// 测试异步执行事件发布
    	if(applicationEventPublisher instanceof SimpleApplicationEventMulticaster) {
			((SimpleApplicationEventMulticaster)applicationEventPublisher).setTaskExecutor((command) -> {
				System.out.println("异步开始执行");
				command.run();
				System.out.println("异步执行结束");
			});
		}

        this.publisher = applicationEventPublisher;
    }

    public void publishMyEvent(){
		publisher.publishEvent(new MyEvent(this));
	}

}
