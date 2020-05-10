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

package org.springframework.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Internal helper class for converting property values to target types.
 *
 * <p>Works on a given {@link PropertyEditorRegistrySupport} instance.
 * Used as a delegate by {@link BeanWrapperImpl} and {@link SimpleTypeConverter}.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Dave Syer
 * @since 2.0
 * @see BeanWrapperImpl
 * @see SimpleTypeConverter
 */
class TypeConverterDelegate {

	private static final Log logger = LogFactory.getLog(TypeConverterDelegate.class);

	private final PropertyEditorRegistrySupport propertyEditorRegistry;

	@Nullable
	private final Object targetObject;


	/**
	 * Create a new TypeConverterDelegate for the given editor registry.
	 * @param propertyEditorRegistry the editor registry to use
	 */
	public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry) {
		this(propertyEditorRegistry, null);
	}

	/**
	 * Create a new TypeConverterDelegate for the given editor registry and bean instance.
	 * @param propertyEditorRegistry the editor registry to use
	 * @param targetObject the target object to work on (as context that can be passed to editors)
	 */
	public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry, @Nullable Object targetObject) {
		this.propertyEditorRegistry = propertyEditorRegistry;
		this.targetObject = targetObject;
	}


	/**
	 * Convert the value to the specified required type.
	 * @param newValue the proposed new value
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @param methodParam the method parameter that is the target of the conversion
	 * (may be {@code null})
	 * @return the new value, possibly the result of type conversion
	 * @throws IllegalArgumentException if type conversion failed
	 */
	@Nullable
	public <T> T convertIfNecessary(@Nullable Object newValue, @Nullable Class<T> requiredType,
			@Nullable MethodParameter methodParam) throws IllegalArgumentException {

		return convertIfNecessary(null, null, newValue, requiredType,
				(methodParam != null ? new TypeDescriptor(methodParam) : TypeDescriptor.valueOf(requiredType)));
	}

	/**
	 * Convert the value to the specified required type.
	 * @param newValue the proposed new value
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @param field the reflective field that is the target of the conversion
	 * (may be {@code null})
	 * @return the new value, possibly the result of type conversion
	 * @throws IllegalArgumentException if type conversion failed
	 */
	@Nullable
	public <T> T convertIfNecessary(@Nullable Object newValue, @Nullable Class<T> requiredType, @Nullable Field field)
			throws IllegalArgumentException {

		return convertIfNecessary(null, null, newValue, requiredType,
				(field != null ? new TypeDescriptor(field) : TypeDescriptor.valueOf(requiredType)));
	}

	/**
	 * Convert the value to the required type for the specified property.
	 * @param propertyName name of the property
	 * @param oldValue the previous value, if available (may be {@code null})
	 * @param newValue the proposed new value
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @return the new value, possibly the result of type conversion
	 * @throws IllegalArgumentException if type conversion failed
	 */
	@Nullable
	public <T> T convertIfNecessary(@Nullable String propertyName, @Nullable Object oldValue,
			Object newValue, @Nullable Class<T> requiredType) throws IllegalArgumentException {

		return convertIfNecessary(propertyName, oldValue, newValue, requiredType, TypeDescriptor.valueOf(requiredType));
	}

	/**
	 * Convert the value to the required type (if necessary from a String),
	 * for the specified property.
	 * @param propertyName name of the property
	 * @param oldValue the previous value, if available (may be {@code null})
	 * @param newValue the proposed new value
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @param typeDescriptor the descriptor for the target property or field
	 * @return the new value, possibly the result of type conversion
	 * @throws IllegalArgumentException if type conversion failed
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public <T> T convertIfNecessary(@Nullable String propertyName, @Nullable Object oldValue, @Nullable Object newValue,
			@Nullable Class<T> requiredType, @Nullable TypeDescriptor typeDescriptor) throws IllegalArgumentException {

		// Custom editor for this type?
		// 获取用户自定义的 属性编辑器
		PropertyEditor editor = this.propertyEditorRegistry.findCustomEditor(requiredType, propertyName);

		ConversionFailedException conversionAttemptEx = null;

		// No custom editor but custom ConversionService specified?
		ConversionService conversionService = this.propertyEditorRegistry.getConversionService();
		// 无自定义属性编辑器 并且存在转换服务的情况，则使用转换服务
		if (editor == null && conversionService != null && newValue != null && typeDescriptor != null) {
			TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
			if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
				try {
					return (T) conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
				} catch (ConversionFailedException ex) {
					// fallback to default conversion logic below
					conversionAttemptEx = ex;
				}
			}
		}

		Object convertedValue = newValue;

		// Value not of required type?
		// 有属性编辑器存在的情况 或 转换的类型不是基本类型或包装类型
		if (editor != null || (requiredType != null && !ClassUtils.isAssignableValue(requiredType, convertedValue))) {
			// 是否要转换为集合类型
			if (typeDescriptor != null && requiredType != null && Collection.class.isAssignableFrom(requiredType) &&
					convertedValue instanceof String) {
				// 获取集合内部的元素类型描述器
				TypeDescriptor elementTypeDesc = typeDescriptor.getElementTypeDescriptor();
				if (elementTypeDesc != null) {
					Class<?> elementType = elementTypeDesc.getType();
					if (Class.class == elementType || Enum.class.isAssignableFrom(elementType)) {
						// 通过[,]切割，获取 String[]
						convertedValue = StringUtils.commaDelimitedListToStringArray((String) convertedValue);
					}
				}
			}
			// 无属性编辑器存在，则获取Spring内置的默认的编辑器
			if (editor == null) {
				editor = findDefaultEditor(requiredType);
			}
			// 通过属性编辑器进行转换操作
			convertedValue = doConvertValue(oldValue, convertedValue, requiredType, editor);
		}

		boolean standardConversion = false;

		if (requiredType != null) {
			// Try to apply some standard type conversion rules if appropriate.
			// 如果可以，尝试使用一些标准类型转换规则
			if (convertedValue != null) {
				if (Object.class == requiredType) {
					return (T) convertedValue;
				}
				// 返回出来的值可能是集合（不一定是String类型的）
				else if (requiredType.isArray()) {
					// Array required -> apply appropriate conversion of elements.
					if (convertedValue instanceof String && Enum.class.isAssignableFrom(requiredType.getComponentType())) {
						convertedValue = StringUtils.commaDelimitedListToStringArray((String) convertedValue);
					}
					return (T) convertToTypedArray(convertedValue, propertyName, requiredType.getComponentType());
				}
				else if (convertedValue instanceof Collection) {
					// Convert elements to target type, if determined.
					convertedValue = convertToTypedCollection(
							(Collection<?>) convertedValue, propertyName, requiredType, typeDescriptor);
					standardConversion = true;
				}
				else if (convertedValue instanceof Map) {
					// Convert keys and values to respective target type, if determined.
					convertedValue = convertToTypedMap(
							(Map<?, ?>) convertedValue, propertyName, requiredType, typeDescriptor);
					standardConversion = true;
				}
				// 数组只有一个值时，直接返回值
				if (convertedValue.getClass().isArray() && Array.getLength(convertedValue) == 1) {
					convertedValue = Array.get(convertedValue, 0);
					standardConversion = true;
				}
				// 基本类型的字符串化（如果有必要）
				if (String.class == requiredType && ClassUtils.isPrimitiveOrWrapper(convertedValue.getClass())) {
					// We can stringify any primitive value...
					return (T) convertedValue.toString();
				}
				// 转换出来的值为String类型，但是需要转换为 的类型不是String
				else if (convertedValue instanceof String && !requiredType.isInstance(convertedValue)) {
					if (conversionAttemptEx == null && !requiredType.isInterface() && !requiredType.isEnum()) {
						try {
							// 通过 获取到的公开构造函数 和 参数值 创建出实例
							Constructor<T> strCtor = requiredType.getConstructor(String.class);
							return BeanUtils.instantiateClass(strCtor, convertedValue);
						}
						catch (NoSuchMethodException ex) {
							// proceed with field lookup
							if (logger.isTraceEnabled()) {
								logger.trace("No String constructor found on type [" + requiredType.getName() + "]", ex);
							}
						}
						catch (Exception ex) {
							if (logger.isDebugEnabled()) {
								logger.debug("Construction via String failed for type [" + requiredType.getName() + "]", ex);
							}
						}
					}
					String trimmedValue = ((String) convertedValue).trim();
					if (requiredType.isEnum() && trimmedValue.isEmpty()) {
						// It's an empty enum identifier: reset the enum value to null.
						return null;
					}
					// 尝试将String转换为Enum类型（如果 requiredType 为枚举类型）
					convertedValue = attemptToConvertStringToEnum(requiredType, trimmedValue, convertedValue);
					standardConversion = true;
				}
				// 将 Number 类型的值根据 requiredType 转换为对应的类型
				else if (convertedValue instanceof Number && Number.class.isAssignableFrom(requiredType)) {
					convertedValue = NumberUtils.convertNumberToTargetClass(
							(Number) convertedValue, (Class<Number>) requiredType);
					standardConversion = true;
				}
			}
			else {
				// convertedValue == null
				if (requiredType == Optional.class) {
					convertedValue = Optional.empty();
				}
			}

			// 转换后的类型和值 不是基本类型或包装类
			if (!ClassUtils.isAssignableValue(requiredType, convertedValue)) {
				// 复杂类型在这抛异常
				if (conversionAttemptEx != null) {
					// Original exception from former ConversionService call above...
					throw conversionAttemptEx;
				}
				// 复杂类型再通过转换服务进行转换（并不能通过找到的 PropertyEditor 获取到需要的类型对象）
				else if (conversionService != null && typeDescriptor != null) {
					// ConversionService not tried before, probably custom editor found
					// but editor couldn't produce the required type...
					TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
					if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
						return (T) conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
					}
				}

				// 实在不能转换只能抛异常了
				// Definitely doesn't match: throw IllegalArgumentException/IllegalStateException
				StringBuilder msg = new StringBuilder();
				msg.append("Cannot convert value of type '").append(ClassUtils.getDescriptiveType(newValue));
				msg.append("' to required type '").append(ClassUtils.getQualifiedName(requiredType)).append("'");
				if (propertyName != null) {
					msg.append(" for property '").append(propertyName).append("'");
				}
				if (editor != null) {
					msg.append(": PropertyEditor [").append(editor.getClass().getName()).append(
							"] returned inappropriate value of type '").append(
							ClassUtils.getDescriptiveType(convertedValue)).append("'");
					throw new IllegalArgumentException(msg.toString());
				}
				else {
					msg.append(": no matching editors or conversion strategy found");
					throw new IllegalStateException(msg.toString());
				}
			}
		}

		// 基本类型在这抛异常
		if (conversionAttemptEx != null) {
			if (editor == null && !standardConversion && requiredType != null && Object.class != requiredType) {
				throw conversionAttemptEx;
			}
			logger.debug("Original ConversionService attempt failed - ignored since " +
					"PropertyEditor based conversion eventually succeeded", conversionAttemptEx);
		}

		return (T) convertedValue;
	}

	private Object attemptToConvertStringToEnum(Class<?> requiredType, String trimmedValue, Object currentConvertedValue) {
		Object convertedValue = currentConvertedValue;

		if (Enum.class == requiredType && this.targetObject != null) {
			// target type is declared as raw enum, treat the trimmed value as <enum.fqn>.FIELD_NAME
			int index = trimmedValue.lastIndexOf('.');
			if (index > - 1) {
				String enumType = trimmedValue.substring(0, index);
				String fieldName = trimmedValue.substring(index + 1);
				ClassLoader cl = this.targetObject.getClass().getClassLoader();
				try {
					Class<?> enumValueType = ClassUtils.forName(enumType, cl);
					Field enumField = enumValueType.getField(fieldName);
					convertedValue = enumField.get(null);
				}
				catch (ClassNotFoundException ex) {
					if (logger.isTraceEnabled()) {
						logger.trace("Enum class [" + enumType + "] cannot be loaded", ex);
					}
				}
				catch (Throwable ex) {
					if (logger.isTraceEnabled()) {
						logger.trace("Field [" + fieldName + "] isn't an enum value for type [" + enumType + "]", ex);
					}
				}
			}
		}

		if (convertedValue == currentConvertedValue) {
			// Try field lookup as fallback: for JDK 1.5 enum or custom enum
			// with values defined as static fields. Resulting value still needs
			// to be checked, hence we don't return it right away.
			try {
				Field enumField = requiredType.getField(trimmedValue);
				ReflectionUtils.makeAccessible(enumField);
				convertedValue = enumField.get(null);
			}
			catch (Throwable ex) {
				if (logger.isTraceEnabled()) {
					logger.trace("Field [" + convertedValue + "] isn't an enum value", ex);
				}
			}
		}

		return convertedValue;
	}
	/**
	 * Find a default editor for the given type.
	 * @param requiredType the type to find an editor for
	 * @return the corresponding editor, or {@code null} if none
	 */
	@Nullable
	private PropertyEditor findDefaultEditor(@Nullable Class<?> requiredType) {
		PropertyEditor editor = null;
		if (requiredType != null) {
			// No custom editor -> check BeanWrapperImpl's default editors.
			// 获取Spring内置的默认属性编辑器
			editor = this.propertyEditorRegistry.getDefaultEditor(requiredType);
			if (editor == null && String.class != requiredType) {
				// No BeanWrapper default editor -> check standard JavaBean editor.
				// 普通JavaBean和对应的 PropertyEditor 位于【同一包】中并且与该JavaBean具有相同的名称，并且带有 Editor 附加名称
				// Person & PersonEditor Spring会自动探测
				editor = BeanUtils.findEditorByConvention(requiredType);
			}
		}
		return editor;
	}

	/**
	 * Convert the value to the required type (if necessary from a String),
	 * using the given property editor.
	 * @param oldValue the previous value, if available (may be {@code null})
	 * @param newValue the proposed new value
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @param editor the PropertyEditor to use
	 * @return the new value, possibly the result of type conversion
	 * @throws IllegalArgumentException if type conversion failed
	 */
	@Nullable
	private Object doConvertValue(@Nullable Object oldValue, @Nullable Object newValue,
			@Nullable Class<?> requiredType, @Nullable PropertyEditor editor) {

		Object convertedValue = newValue;

		// 有默认的属性编辑器 & String 类型不需要转换
		if (editor != null && !(convertedValue instanceof String)) {
			// Not a String -> use PropertyEditor's setValue.
			// With standard PropertyEditors, this will return the very same object;
			// we just want to allow special PropertyEditors to override setValue
			// for type conversion from non-String values to the required type.
			try {
				// 设置属性值的核心方法（非字符串类型的值）
				editor.setValue(convertedValue);
				Object newConvertedValue = editor.getValue();
				if (newConvertedValue != convertedValue) {
					convertedValue = newConvertedValue;
					// Reset PropertyEditor: It already did a proper conversion.
					// Don't use it again for a setAsText call.
					editor = null;
				}
			}
			catch (Exception ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
				}
				// Swallow and proceed.
			}
		}

		Object returnValue = convertedValue;

		// 处理 String 数组
		if (requiredType != null && !requiredType.isArray() && convertedValue instanceof String[]) {
			// Convert String array to a comma-separated String.
			// Only applies if no PropertyEditor converted the String array before.
			// The CSV String will be passed into a PropertyEditor's setAsText method, if any.
			if (logger.isTraceEnabled()) {
				logger.trace("Converting String array to comma-delimited String [" + convertedValue + "]");
			}
			// String 通过[,] 分割
			convertedValue = StringUtils.arrayToCommaDelimitedString((String[]) convertedValue);
		}

		// 处理 String 类型
		if (convertedValue instanceof String) {
			if (editor != null) {
				// Use PropertyEditor's setAsText in case of a String value.
				if (logger.isTraceEnabled()) {
					logger.trace("Converting String to [" + requiredType + "] using property editor [" + editor + "]");
				}
				String newTextValue = (String) convertedValue;
				// 处理字符串类型（要处理）的值
				return doConvertTextValue(oldValue, newTextValue, editor);
			}
			else if (String.class == requiredType) {
				returnValue = convertedValue;
			}
		}

		return returnValue;
	}

	/**
	 * Convert the given text value using the given property editor.
	 * @param oldValue the previous value, if available (may be {@code null})
	 * @param newTextValue the proposed text value
	 * @param editor the PropertyEditor to use
	 * @return the converted value
	 */
	private Object doConvertTextValue(@Nullable Object oldValue, String newTextValue, PropertyEditor editor) {
		try {
			// 有旧的值，先设置一下
			editor.setValue(oldValue);
		}
		catch (Exception ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
			}
			// Swallow and proceed.
		}
		// 设置新的值，内部还是通过 setValue() 方法设置。中间就是多了一层我们【自定义】的值/类型转换操作
		// 自定义属性编辑器一般只要继承 PropertyEditorSupport，重写 此方法即可
		editor.setAsText(newTextValue);
		// 返回处理过的值
		return editor.getValue();
	}

	private Object convertToTypedArray(Object input, @Nullable String propertyName, Class<?> componentType) {
		if (input instanceof Collection) {
			// Convert Collection elements to array elements.
			Collection<?> coll = (Collection<?>) input;
			Object result = Array.newInstance(componentType, coll.size());
			int i = 0;
			for (Iterator<?> it = coll.iterator(); it.hasNext(); i++) {
				Object value = convertIfNecessary(
						buildIndexedPropertyName(propertyName, i), null, it.next(), componentType);
				Array.set(result, i, value);
			}
			return result;
		}
		else if (input.getClass().isArray()) {
			// Convert array elements, if necessary.
			if (componentType.equals(input.getClass().getComponentType()) &&
					!this.propertyEditorRegistry.hasCustomEditorForElement(componentType, propertyName)) {
				return input;
			}
			int arrayLength = Array.getLength(input);
			Object result = Array.newInstance(componentType, arrayLength);
			for (int i = 0; i < arrayLength; i++) {
				Object value = convertIfNecessary(
						buildIndexedPropertyName(propertyName, i), null, Array.get(input, i), componentType);
				Array.set(result, i, value);
			}
			return result;
		}
		else {
			// A plain value: convert it to an array with a single component.
			Object result = Array.newInstance(componentType, 1);
			Object value = convertIfNecessary(
					buildIndexedPropertyName(propertyName, 0), null, input, componentType);
			Array.set(result, 0, value);
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<?> convertToTypedCollection(Collection<?> original, @Nullable String propertyName,
			Class<?> requiredType, @Nullable TypeDescriptor typeDescriptor) {

		if (!Collection.class.isAssignableFrom(requiredType)) {
			return original;
		}

		boolean approximable = CollectionFactory.isApproximableCollectionType(requiredType);
		if (!approximable && !canCreateCopy(requiredType)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Custom Collection type [" + original.getClass().getName() +
						"] does not allow for creating a copy - injecting original Collection as-is");
			}
			return original;
		}

		boolean originalAllowed = requiredType.isInstance(original);
		TypeDescriptor elementType = (typeDescriptor != null ? typeDescriptor.getElementTypeDescriptor() : null);
		if (elementType == null && originalAllowed &&
				!this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
			return original;
		}

		Iterator<?> it;
		try {
			it = original.iterator();
		}
		catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Cannot access Collection of type [" + original.getClass().getName() +
						"] - injecting original Collection as-is: " + ex);
			}
			return original;
		}

		Collection<Object> convertedCopy;
		try {
			if (approximable) {
				convertedCopy = CollectionFactory.createApproximateCollection(original, original.size());
			}
			else {
				convertedCopy = (Collection<Object>)
						ReflectionUtils.accessibleConstructor(requiredType).newInstance();
			}
		}
		catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Cannot create copy of Collection type [" + original.getClass().getName() +
						"] - injecting original Collection as-is: " + ex);
			}
			return original;
		}

		int i = 0;
		for (; it.hasNext(); i++) {
			Object element = it.next();
			String indexedPropertyName = buildIndexedPropertyName(propertyName, i);
			Object convertedElement = convertIfNecessary(indexedPropertyName, null, element,
					(elementType != null ? elementType.getType() : null) , elementType);
			try {
				convertedCopy.add(convertedElement);
			}
			catch (Throwable ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Collection type [" + original.getClass().getName() +
							"] seems to be read-only - injecting original Collection as-is: " + ex);
				}
				return original;
			}
			originalAllowed = originalAllowed && (element == convertedElement);
		}
		return (originalAllowed ? original : convertedCopy);
	}

	@SuppressWarnings("unchecked")
	private Map<?, ?> convertToTypedMap(Map<?, ?> original, @Nullable String propertyName,
			Class<?> requiredType, @Nullable TypeDescriptor typeDescriptor) {

		if (!Map.class.isAssignableFrom(requiredType)) {
			return original;
		}

		boolean approximable = CollectionFactory.isApproximableMapType(requiredType);
		if (!approximable && !canCreateCopy(requiredType)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Custom Map type [" + original.getClass().getName() +
						"] does not allow for creating a copy - injecting original Map as-is");
			}
			return original;
		}

		boolean originalAllowed = requiredType.isInstance(original);
		TypeDescriptor keyType = (typeDescriptor != null ? typeDescriptor.getMapKeyTypeDescriptor() : null);
		TypeDescriptor valueType = (typeDescriptor != null ? typeDescriptor.getMapValueTypeDescriptor() : null);
		if (keyType == null && valueType == null && originalAllowed &&
				!this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
			return original;
		}

		Iterator<?> it;
		try {
			it = original.entrySet().iterator();
		}
		catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Cannot access Map of type [" + original.getClass().getName() +
						"] - injecting original Map as-is: " + ex);
			}
			return original;
		}

		Map<Object, Object> convertedCopy;
		try {
			if (approximable) {
				convertedCopy = CollectionFactory.createApproximateMap(original, original.size());
			}
			else {
				convertedCopy = (Map<Object, Object>)
						ReflectionUtils.accessibleConstructor(requiredType).newInstance();
			}
		}
		catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Cannot create copy of Map type [" + original.getClass().getName() +
						"] - injecting original Map as-is: " + ex);
			}
			return original;
		}

		while (it.hasNext()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			String keyedPropertyName = buildKeyedPropertyName(propertyName, key);
			Object convertedKey = convertIfNecessary(keyedPropertyName, null, key,
					(keyType != null ? keyType.getType() : null), keyType);
			Object convertedValue = convertIfNecessary(keyedPropertyName, null, value,
					(valueType!= null ? valueType.getType() : null), valueType);
			try {
				convertedCopy.put(convertedKey, convertedValue);
			}
			catch (Throwable ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Map type [" + original.getClass().getName() +
							"] seems to be read-only - injecting original Map as-is: " + ex);
				}
				return original;
			}
			originalAllowed = originalAllowed && (key == convertedKey) && (value == convertedValue);
		}
		return (originalAllowed ? original : convertedCopy);
	}

	@Nullable
	private String buildIndexedPropertyName(@Nullable String propertyName, int index) {
		return (propertyName != null ?
				propertyName + PropertyAccessor.PROPERTY_KEY_PREFIX + index + PropertyAccessor.PROPERTY_KEY_SUFFIX :
				null);
	}

	@Nullable
	private String buildKeyedPropertyName(@Nullable String propertyName, Object key) {
		return (propertyName != null ?
				propertyName + PropertyAccessor.PROPERTY_KEY_PREFIX + key + PropertyAccessor.PROPERTY_KEY_SUFFIX :
				null);
	}

	private boolean canCreateCopy(Class<?> requiredType) {
		return (!requiredType.isInterface() && !Modifier.isAbstract(requiredType.getModifiers()) &&
				Modifier.isPublic(requiredType.getModifiers()) && ClassUtils.hasConstructor(requiredType));
	}

}
