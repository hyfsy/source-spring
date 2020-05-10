package com.hyf.mvc.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * @author baB_hyf
 * @date 2020/05/05
 */
public class StringToNumber implements Converter<String, Number> {

	@Override
	public Number convert(String source) {
		return Integer.valueOf(source);
	}
}
