package com.hyf.test;

import org.junit.Test;

import java.util.Locale;

/**
 * @author baB_hyf
 * @date 2020/05/11
 */
public class LocaleTests {

	@Test
	public void testLocale() {
		// 语言 国家 语言环境变体代码
		Locale locale = new Locale("zh", "CN", "asdf");
//		Locale locale = Locale.getDefault();
		System.out.println(locale.getLanguage());
		System.out.println(locale.getCountry());
		System.out.println(locale.getVariant());
	}
}
