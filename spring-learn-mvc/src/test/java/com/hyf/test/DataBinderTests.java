package com.hyf.test;

import com.hyf.mvc.pojo.Person;
import org.junit.Test;
import org.springframework.beans.*;
import org.springframework.validation.DataBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2020/05/08
 */
public class DataBinderTests {

	@Test
	public void testDataBinder() {
		Person person = new Person();
		DataBinder binder = new DataBinder(person);
//		binder.setAllowedFields("*id");
		binder.setDisallowedFields("age*", "na*me");
		Map<String, Object> personMap = new HashMap<>();
		personMap.put("id", 1);
		personMap.put("name", "zs");
		personMap.put("age", 20);
		MutablePropertyValues propertyValues = new MutablePropertyValues(personMap);
		binder.bind(propertyValues);
		System.out.println(person);
	}

	@Test
	public void testPropertyAccessor() {
	    String propertyPath = "map[key][v][v2]";
		List<String> paths = new ArrayList<>();
//		PropertyEditorRegistrySupport.addStrippedPropertyPaths(paths, "", propertyPath);
		System.out.println(paths);
	}
}
