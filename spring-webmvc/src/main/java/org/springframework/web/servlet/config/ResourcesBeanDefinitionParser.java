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

package org.springframework.web.servlet.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.resource.CachingResourceResolver;
import org.springframework.web.servlet.resource.CachingResourceTransformer;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
import org.springframework.web.servlet.resource.FixedVersionStrategy;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

/**
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser} that parses a
 * {@code resources} element to register a {@link ResourceHttpRequestHandler} and
 * register a {@link SimpleUrlHandlerMapping} for mapping resource requests, and a
 * {@link HttpRequestHandlerAdapter}. Will also create a resource handling chain with
 * {@link ResourceResolver ResourceResolvers} and {@link ResourceTransformer ResourceTransformers}.
 *
 * @author Keith Donald
 * @author Jeremy Grelle
 * @author Brian Clozel
 * @since 3.0.4
 */
class ResourcesBeanDefinitionParser implements BeanDefinitionParser {

	private static final String RESOURCE_CHAIN_CACHE = "spring-resource-chain-cache";

	private static final String VERSION_RESOLVER_ELEMENT = "version-resolver";

	private static final String VERSION_STRATEGY_ELEMENT = "version-strategy";

	private static final String FIXED_VERSION_STRATEGY_ELEMENT = "fixed-version-strategy";

	private static final String CONTENT_VERSION_STRATEGY_ELEMENT = "content-version-strategy";

	private static final String RESOURCE_URL_PROVIDER = "mvcResourceUrlProvider";

	private static final boolean isWebJarsAssetLocatorPresent = ClassUtils.isPresent(
			"org.webjars.WebJarAssetLocator", ResourcesBeanDefinitionParser.class.getClassLoader());


	@Override
	public BeanDefinition parse(Element element, ParserContext context) {
		Object source = context.extractSource(element);

		// 注册 ResourceUrlProvider
		registerUrlProvider(context, source);

		// 注册 AntPathMatcher & UrlPathHelper
		RuntimeBeanReference pathMatcherRef = MvcNamespaceUtils.registerPathMatcher(null, context, source);
		RuntimeBeanReference pathHelperRef = MvcNamespaceUtils.registerUrlPathHelper(null, context, source);

		// 注册 ResourceHttpRequestHandler
		String resourceHandlerName = registerResourceHandler(context, element, pathHelperRef, source);
		if (resourceHandlerName == null) {
			return null;
		}

		Map<String, String> urlMap = new ManagedMap<>();
		String resourceRequestPath = element.getAttribute("mapping");
		if (!StringUtils.hasText(resourceRequestPath)) {
			context.getReaderContext().error("The 'mapping' attribute is required.", context.extractSource(element));
			return null;
		}

		// location路径 和 HttpRequestHandler bean名称绑定
		urlMap.put(resourceRequestPath, resourceHandlerName);

		// 注册 SimpleUrlHandlerMapping
		RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
		handlerMappingDef.setSource(source);
		handlerMappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		handlerMappingDef.getPropertyValues().add("urlMap", urlMap);
		handlerMappingDef.getPropertyValues().add("pathMatcher", pathMatcherRef).add("urlPathHelper", pathHelperRef);

		String orderValue = element.getAttribute("order");
		// Use a default of near-lowest precedence, still allowing for even lower precedence in other mappings
		Object order = StringUtils.hasText(orderValue) ? orderValue : Ordered.LOWEST_PRECEDENCE - 1;
		handlerMappingDef.getPropertyValues().add("order", order);

		// CorsConfigurations 实际为一个 LinkedHashMap
		RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
		handlerMappingDef.getPropertyValues().add("corsConfigurations", corsRef);

		String beanName = context.getReaderContext().generateBeanName(handlerMappingDef);
		context.getRegistry().registerBeanDefinition(beanName, handlerMappingDef);
		context.registerComponent(new BeanComponentDefinition(handlerMappingDef, beanName));

		// Ensure BeanNameUrlHandlerMapping (SPR-8289) and default HandlerAdapters are not "turned off"
		// Register HttpRequestHandlerAdapter
		MvcNamespaceUtils.registerDefaultComponents(context, source);

		return null;
	}

	private void registerUrlProvider(ParserContext context, @Nullable Object source) {
		if (!context.getRegistry().containsBeanDefinition(RESOURCE_URL_PROVIDER)) {
			// 上下文刷新事件时，回调处理 SimpleUrlHandlerMapping 的 监听器
			RootBeanDefinition urlProvider = new RootBeanDefinition(ResourceUrlProvider.class);
			urlProvider.setSource(source);
			urlProvider.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			context.getRegistry().registerBeanDefinition(RESOURCE_URL_PROVIDER, urlProvider);
			context.registerComponent(new BeanComponentDefinition(urlProvider, RESOURCE_URL_PROVIDER));

			// 该拦截器用于将上面的对象与请求绑定
			RootBeanDefinition interceptor = new RootBeanDefinition(ResourceUrlProviderExposingInterceptor.class);
			interceptor.setSource(source);
			interceptor.getConstructorArgumentValues().addIndexedArgumentValue(0, urlProvider);

			// 处理 include/exclude 匹配拦截器，内部还是会委托实际的拦截器进行处理
			RootBeanDefinition mappedInterceptor = new RootBeanDefinition(MappedInterceptor.class);
			mappedInterceptor.setSource(source);
			mappedInterceptor.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			mappedInterceptor.getConstructorArgumentValues().addIndexedArgumentValue(0, (Object) null);
			mappedInterceptor.getConstructorArgumentValues().addIndexedArgumentValue(1, interceptor);
			String mappedInterceptorName = context.getReaderContext().registerWithGeneratedName(mappedInterceptor);
			context.registerComponent(new BeanComponentDefinition(mappedInterceptor, mappedInterceptorName));
		}
	}

	@Nullable
	private String registerResourceHandler(ParserContext context, Element element,
			RuntimeBeanReference pathHelperRef, @Nullable Object source) {

		/*
		 * ResourceHttpRequestHandler
		 * |- UrlPathHelper
		 * |- locationValues
		 * |- cacheSeconds
		 * |- CacheControl
		 * |
		 * |-> resourceResolvers
		 * |-> resourceTransformers
		 * ------------------------------------------
		 * |- parseResourceCache()
		 * 	————|- CachingResourceResolver
		 * 		|- CachingResourceTransformer
		 * 		|- 标签自定义 | ConcurrentMapCache
		 * |- parseResourceResolversTransformers()
		 * 	————|- VersionResourceResolver
		 * |	 ———|- FixedVersionStrategy
		 * |		|- ContentVersionStrategy
		 * |		|- 自定义 VersionStrategy
		 * 	————|- CssLinkResourceTransformer - auto
		 * |	|- WebJarsResourceResolver -auto
		 * |	|- PathResourceResolver - auto
		 * |	|- 自定义 ResourceTransformer
		 * ------------------------------------------
		 * |
		 * |- ContentNegotiationManager
		 */


		String locationAttr = element.getAttribute("location");
		if (!StringUtils.hasText(locationAttr)) {
			context.getReaderContext().error("The 'location' attribute is required.", context.extractSource(element));
			return null;
		}

		// ResourceHttpRequestHandler bean定义
		RootBeanDefinition resourceHandlerDef = new RootBeanDefinition(ResourceHttpRequestHandler.class);
		resourceHandlerDef.setSource(source);
		resourceHandlerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

		// 设置属性值
		MutablePropertyValues values = resourceHandlerDef.getPropertyValues();
		values.add("urlPathHelper", pathHelperRef);
		values.add("locationValues", StringUtils.commaDelimitedListToStringArray(locationAttr));

		String cacheSeconds = element.getAttribute("cache-period");
		if (StringUtils.hasText(cacheSeconds)) {
			values.add("cacheSeconds", cacheSeconds);
		}

		// 解析 CacheControl 对象，并设置为 ResourceHttpRequestHandler 的属性
		Element cacheControlElement = DomUtils.getChildElementByTagName(element, "cache-control");
		if (cacheControlElement != null) {
			CacheControl cacheControl = parseCacheControl(cacheControlElement);
			values.add("cacheControl", cacheControl);
		}

		// 解析 mvc:resource-chain 标签
		Element resourceChainElement = DomUtils.getChildElementByTagName(element, "resource-chain");
		if (resourceChainElement != null) {
			parseResourceChain(resourceHandlerDef, context, resourceChainElement, source);
		}

		// 设置 ContentNegotiationManager 属性
		Object manager = MvcNamespaceUtils.getContentNegotiationManager(context);
		if (manager != null) {
			values.add("contentNegotiationManager", manager);
		}

		String beanName = context.getReaderContext().generateBeanName(resourceHandlerDef);
		context.getRegistry().registerBeanDefinition(beanName, resourceHandlerDef);
		context.registerComponent(new BeanComponentDefinition(resourceHandlerDef, beanName));
		return beanName;
	}


	private CacheControl parseCacheControl(Element element) {

		// 四种方式构造一个 CacheControl 对象
		CacheControl cacheControl;
		if ("true".equals(element.getAttribute("no-cache"))) {
			cacheControl = CacheControl.noCache();
		}
		else if ("true".equals(element.getAttribute("no-store"))) {
			cacheControl = CacheControl.noStore();
		}
		else if (element.hasAttribute("max-age")) {
			cacheControl = CacheControl.maxAge(Long.parseLong(element.getAttribute("max-age")), TimeUnit.SECONDS);
		}
		else {
			cacheControl = CacheControl.empty();
		}

		// 添加属性
		if ("true".equals(element.getAttribute("must-revalidate"))) {
			cacheControl = cacheControl.mustRevalidate();
		}
		if ("true".equals(element.getAttribute("no-transform"))) {
			cacheControl = cacheControl.noTransform();
		}
		if ("true".equals(element.getAttribute("cache-public"))) {
			cacheControl = cacheControl.cachePublic();
		}
		if ("true".equals(element.getAttribute("cache-private"))) {
			cacheControl = cacheControl.cachePrivate();
		}
		if ("true".equals(element.getAttribute("proxy-revalidate"))) {
			cacheControl = cacheControl.proxyRevalidate();
		}
		if (element.hasAttribute("s-maxage")) {
			cacheControl = cacheControl.sMaxAge(Long.parseLong(element.getAttribute("s-maxage")), TimeUnit.SECONDS);
		}
		if (element.hasAttribute("stale-while-revalidate")) {
			cacheControl = cacheControl.staleWhileRevalidate(
					Long.parseLong(element.getAttribute("stale-while-revalidate")), TimeUnit.SECONDS);
		}
		if (element.hasAttribute("stale-if-error")) {
			cacheControl = cacheControl.staleIfError(
					Long.parseLong(element.getAttribute("stale-if-error")), TimeUnit.SECONDS);
		}
		return cacheControl;
	}

	private void parseResourceChain(
			RootBeanDefinition resourceHandlerDef, ParserContext context, Element element, @Nullable Object source) {

		// element: mvc:resource-chain
		String autoRegistration = element.getAttribute("auto-registration");
		boolean isAutoRegistration = !(StringUtils.hasText(autoRegistration) && "false".equals(autoRegistration));

		ManagedList<Object> resourceResolvers = new ManagedList<>();
		resourceResolvers.setSource(source);
		ManagedList<Object> resourceTransformers = new ManagedList<>();
		resourceTransformers.setSource(source);

		// 解析 mvc:resource-chain 标签上的属性值（缓存管理）
		parseResourceCache(resourceResolvers, resourceTransformers, element, source);
		// 解析 mvc:resolvers 和 mvc:transformers 标签
		parseResourceResolversTransformers(
				isAutoRegistration, resourceResolvers, resourceTransformers, context, element, source);

		// ResourceHttpRequestHandler 添加两个属性
		if (!resourceResolvers.isEmpty()) {
			resourceHandlerDef.getPropertyValues().add("resourceResolvers", resourceResolvers);
		}
		if (!resourceTransformers.isEmpty()) {
			resourceHandlerDef.getPropertyValues().add("resourceTransformers", resourceTransformers);
		}
	}

	private void parseResourceCache(ManagedList<Object> resourceResolvers,
			ManagedList<Object> resourceTransformers, Element element, @Nullable Object source) {

		String resourceCache = element.getAttribute("resource-cache");
		if ("true".equals(resourceCache)) {
			ConstructorArgumentValues cargs = new ConstructorArgumentValues();

			// CachingResourceResolver bean定义
			RootBeanDefinition cachingResolverDef = new RootBeanDefinition(CachingResourceResolver.class);
			cachingResolverDef.setSource(source);
			cachingResolverDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			cachingResolverDef.setConstructorArgumentValues(cargs);

			// CachingResourceTransformer bean定义
			RootBeanDefinition cachingTransformerDef = new RootBeanDefinition(CachingResourceTransformer.class);
			cachingTransformerDef.setSource(source);
			cachingTransformerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			cachingTransformerDef.setConstructorArgumentValues(cargs);

			// 设置自定义缓存引用
			String cacheManagerName = element.getAttribute("cache-manager");
			String cacheName = element.getAttribute("cache-name");
			if (StringUtils.hasText(cacheManagerName) && StringUtils.hasText(cacheName)) {
				RuntimeBeanReference cacheManagerRef = new RuntimeBeanReference(cacheManagerName);
				cargs.addIndexedArgumentValue(0, cacheManagerRef);
				cargs.addIndexedArgumentValue(1, cacheName);
			}
			else {
				// 默认缓存 ConcurrentMapCache
				ConstructorArgumentValues cacheCavs = new ConstructorArgumentValues();
				cacheCavs.addIndexedArgumentValue(0, RESOURCE_CHAIN_CACHE);
				RootBeanDefinition cacheDef = new RootBeanDefinition(ConcurrentMapCache.class);
				cacheDef.setSource(source);
				cacheDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				cacheDef.setConstructorArgumentValues(cacheCavs);
				// 设置缓存引用
				cargs.addIndexedArgumentValue(0, cacheDef);
			}
			resourceResolvers.add(cachingResolverDef);
			resourceTransformers.add(cachingTransformerDef);
		}
	}

	private void parseResourceResolversTransformers(boolean isAutoRegistration,
			ManagedList<Object> resourceResolvers, ManagedList<Object> resourceTransformers,
			ParserContext context, Element element, @Nullable Object source) {

		Element resolversElement = DomUtils.getChildElementByTagName(element, "resolvers");
		if (resolversElement != null) {
			for (Element beanElement : DomUtils.getChildElements(resolversElement)) {
				// 解析 VersionResourceResolver
				if (VERSION_RESOLVER_ELEMENT.equals(beanElement.getLocalName())) {
					RootBeanDefinition versionResolverDef = parseVersionResolver(context, beanElement, source);
					versionResolverDef.setSource(source);
					resourceResolvers.add(versionResolverDef);
					// 自动注册的情况下注册 CssLinkResourceTransformer
					if (isAutoRegistration) {
						RootBeanDefinition cssLinkTransformerDef = new RootBeanDefinition(CssLinkResourceTransformer.class);
						cssLinkTransformerDef.setSource(source);
						cssLinkTransformerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
						resourceTransformers.add(cssLinkTransformerDef);
					}
				}
				else {
					// 注册自定义 ResourceResolver
					Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
					resourceResolvers.add(object);
				}
			}
		}

		// 自动注册的情况下注册 WebJarsResourceResolver 和 PathResourceResolver
		if (isAutoRegistration) {
			if (isWebJarsAssetLocatorPresent) {
				RootBeanDefinition webJarsResolverDef = new RootBeanDefinition(WebJarsResourceResolver.class);
				webJarsResolverDef.setSource(source);
				webJarsResolverDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				resourceResolvers.add(webJarsResolverDef);
			}
			RootBeanDefinition pathResolverDef = new RootBeanDefinition(PathResourceResolver.class);
			pathResolverDef.setSource(source);
			pathResolverDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			resourceResolvers.add(pathResolverDef);
		}

		// 注册自定义 ResourceTransformer
		Element transformersElement = DomUtils.getChildElementByTagName(element, "transformers");
		if (transformersElement != null) {
			for (Element beanElement : DomUtils.getChildElementsByTagName(transformersElement, "bean", "ref")) {
				Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
				resourceTransformers.add(object);
			}
		}
	}

	private RootBeanDefinition parseVersionResolver(ParserContext context, Element element, @Nullable Object source) {
		ManagedMap<String, Object> strategyMap = new ManagedMap<>();
		strategyMap.setSource(source);
		RootBeanDefinition versionResolverDef = new RootBeanDefinition(VersionResourceResolver.class);
		versionResolverDef.setSource(source);
		versionResolverDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		versionResolverDef.getPropertyValues().addPropertyValue("strategyMap", strategyMap);

		// 遍历解析 xxx-strategy 标签
		for (Element beanElement : DomUtils.getChildElements(element)) {
			String[] patterns = StringUtils.commaDelimitedListToStringArray(beanElement.getAttribute("patterns"));
			Object strategy = null;
			// 注册 FixedVersionStrategy
			if (FIXED_VERSION_STRATEGY_ELEMENT.equals(beanElement.getLocalName())) {
				ConstructorArgumentValues cargs = new ConstructorArgumentValues();
				cargs.addIndexedArgumentValue(0, beanElement.getAttribute("version"));
				RootBeanDefinition strategyDef = new RootBeanDefinition(FixedVersionStrategy.class);
				strategyDef.setSource(source);
				strategyDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				strategyDef.setConstructorArgumentValues(cargs);
				strategy = strategyDef;
			}
			// 注册 ContentVersionStrategy
			else if (CONTENT_VERSION_STRATEGY_ELEMENT.equals(beanElement.getLocalName())) {
				RootBeanDefinition strategyDef = new RootBeanDefinition(ContentVersionStrategy.class);
				strategyDef.setSource(source);
				strategyDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				strategy = strategyDef;
			}
			// 注册自定义 VersionStrategy
			else if (VERSION_STRATEGY_ELEMENT.equals(beanElement.getLocalName())) {
				Element childElement = DomUtils.getChildElementsByTagName(beanElement, "bean", "ref").get(0);
				strategy = context.getDelegate().parsePropertySubElement(childElement, null);
			}
			for (String pattern : patterns) {
				strategyMap.put(pattern, strategy);
			}
		}

		return versionResolverDef;
	}

}
