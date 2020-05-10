package com.hyf.util;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2020/03/28
 */
public class MyUtil {

	@Test
	public void testStringUtils() {
		List<String> list = new ArrayList<String>(){{add("a");add("b");add("c");}};
		String mergeStr = StringUtils.collectionToDelimitedString(list, ";");
		List<String> stringList = Arrays.asList(StringUtils.delimitedListToStringArray(mergeStr, ";"));

		System.out.println(mergeStr);
		System.out.println(stringList);

		String[] delimited = StringUtils.delimitedListToStringArray("a,b,", ",");
		System.out.println(delimited.length);
		System.out.println(Arrays.asList(delimited));

	}

	@Test
	public void testObjectUtils() {
//		ObjectUtils.nullSafe
		String s = ObjectUtils.identityToString(this);
		System.out.println(s);
	}

	@Test
	public void testBeanUtils() {
		BeanUtils.isSimpleProperty(String.class);
	}

	@Test
	public void testUtils() {
	}

	@Test
	public void testClassUtils() {
		String shortName = ClassUtils.getShortName("java.lang.String");
		System.out.println(shortName);
		String shortName1 = ClassUtils.getShortName(MyUtil.class);
		System.out.println(shortName1);
	}
}
