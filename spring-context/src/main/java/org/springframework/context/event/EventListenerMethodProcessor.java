/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/**
 * Registers {@link EventListener} methods as individual {@link ApplicationListener} instances.
 * Implements {@link BeanFactoryPostProcessor} (as of 5.1) primarily for early retrieval,
 * avoiding AOP checks for this processor bean and its {@link EventListenerFactory} delegates.
 *
 * @author Stephane Nicoll
 * @author Juergen Hoeller
 * @since 4.2
 * @see EventListenerFactory
 * @see DefaultEventListenerFactory
 */
public class EventListenerMethodProcessor
		implements SmartInitializingSingleton, ApplicationContextAware, BeanFactoryPostProcessor {

	protected final Log logger = LogFactory.getLog(getClass());

	@Nullable
	private ConfigurableApplicationContext applicationContext;

	@Nullable
	private ConfigurableListableBeanFactory beanFactory;

	@Nullable
	private List<EventListenerFactory> eventListenerFactories;

	private final EventExpressionEvaluator evaluator = new EventExpressionEvaluator();

	private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext,
				"ApplicationContext does not implement ConfigurableApplicationContext");
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;

		// 从容器中获取 EventListenerFactory 类型的对象，非急切实例化（单例池中最终的和现在返回的实例不是同一个）
		Map<String, EventListenerFactory> beans = beanFactory.getBeansOfType(EventListenerFactory.class, false, false);
		List<EventListenerFactory> factories = new ArrayList<>(beans.values());
		AnnotationAwareOrderComparator.sort(factories);
		// 初始化 EventListenerFactory
		this.eventListenerFactories = factories;
	}


	@Override
	public void afterSingletonsInstantiated() {
		ConfigurableListableBeanFactory beanFactory = this.beanFactory;
		Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");
		String[] beanNames = beanFactory.getBeanNamesForType(Object.class);
		for (String beanName : beanNames) {
			if (!ScopedProxyUtils.isScopedTarget(beanName)) {
				Class<?> type = null;
				try {
					type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
				}
				catch (Throwable ex) {
					// An unresolvable bean type, probably from a lazy bean - let's ignore it.
					if (logger.isDebugEnabled()) {
						logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
					}
				}
				if (type != null) {
					// ScopedProxyFactoryBean 生产出的对象都可以转换为此类型
					if (ScopedObject.class.isAssignableFrom(type)) {
						try {
							// 获取实际的目标对象类型
							Class<?> targetClass = AutoProxyUtils.determineTargetClass(
									beanFactory, ScopedProxyUtils.getTargetBeanName(beanName));
							if (targetClass != null) {
								type = targetClass;
							}
						}
						catch (Throwable ex) {
							// An invalid scoped proxy arrangement - let's ignore it.
							if (logger.isDebugEnabled()) {
								logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
							}
						}
					}
					try {
						// 处理bean
						processBean(beanName, type);
					}
					catch (Throwable ex) {
						throw new BeanInitializationException("Failed to process @EventListener " +
								"annotation on bean with name '" + beanName + "'", ex);
					}
				}
			}
		}
	}

	private void processBean(final String beanName, final Class<?> targetType) {
		// 类中没有 EventListener 注解的集合 & 不是组件bean(@Component)
		if (!this.nonAnnotatedClasses.contains(targetType) && !isSpringContainerClass(targetType)) {
			//原本方法 与 注解的代理对象
			Map<Method, EventListener> annotatedMethods = null;
			try {
				// 查找 targetType 类中注释了 EventListener 注解的方法，并生成代理该注解的类，返回映射关系
				annotatedMethods = MethodIntrospector.selectMethods(targetType,
						(MethodIntrospector.MetadataLookup<EventListener>) method ->
								AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class));
			}
			catch (Throwable ex) {
				// An unresolvable type in a method signature, probably from a lazy bean - let's ignore it.
				if (logger.isDebugEnabled()) {
					logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
				}
			}
			// 类 [中] 没有指定注解，放入无注解集合中
			if (CollectionUtils.isEmpty(annotatedMethods)) {
				this.nonAnnotatedClasses.add(targetType);
				if (logger.isTraceEnabled()) {
					logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
				}
			}
			else {
				// Non-empty set of methods
				ConfigurableApplicationContext context = this.applicationContext;
				Assert.state(context != null, "No ApplicationContext set");
				// 实例化时设置
				List<EventListenerFactory> factories = this.eventListenerFactories;
				Assert.state(factories != null, "EventListenerFactory List not initialized");
				for (Method method : annotatedMethods.keySet()) {
					for (EventListenerFactory factory : factories) {
						// 默认工厂默认返回 true，事务工厂会判断是否有指定注解
						if (factory.supportsMethod(method)) {
							Method methodToUse = AopUtils.selectInvocableMethod(method, context.getType(beanName));
							// 通过工厂创建一个 ApplicationListenerMethodAdapter 对象
							ApplicationListener<?> applicationListener =
									factory.createApplicationListener(beanName, targetType, methodToUse);
							if (applicationListener instanceof ApplicationListenerMethodAdapter) {
								// 适配器对象初始化内部的上下文和 condition 的 SpEL评估器
								((ApplicationListenerMethodAdapter) applicationListener).init(context, this.evaluator);
							}
							// 添加事件监听器对象
							context.addApplicationListener(applicationListener);
							break;
						}
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" +
							beanName + "': " + annotatedMethods);
				}
			}
		}
	}

	/**
	 * Determine whether the given class is an {@code org.springframework}
	 * bean class that is not annotated as a user or test {@link Component}...
	 * which indicates that there is no {@link EventListener} to be found there.
	 * @since 5.1
	 */
	private static boolean isSpringContainerClass(Class<?> clazz) {
		return (clazz.getName().startsWith("org.springframework.") &&
				!AnnotatedElementUtils.isAnnotated(ClassUtils.getUserClass(clazz), Component.class));
	}

}
