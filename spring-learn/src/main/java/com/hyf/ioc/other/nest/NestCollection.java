package com.hyf.ioc.other.nest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2020/03/27
 */
public class NestCollection {

	private List<String> nestList = new ArrayList<>();

	private Map<String, Integer> nestMap = new HashMap<>();

	public List<String> getNestList() {
		return nestList;
	}

	public void setNestList(List<String> nestList) {
		this.nestList = nestList;
	}

	public Map<String, Integer> getNestMap() {
		return nestMap;
	}

	public void setNestMap(Map<String, Integer> nestMap) {
		this.nestMap = nestMap;
	}

	@Override
	public String toString() {
		return "NestCollection{" +
				"nestList=" + nestList +
				", nestMap=" + nestMap +
				'}';
	}

}
