package com.hyf.message.order;

import java.util.Map;

/**
 * @author baB_hyf
 * @date 2020/10/17
 */
public interface IOrderManager {

	void placeOrder(Map<String, Object> order);

}
