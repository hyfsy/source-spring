package com.hyf.tx.other.service;

import com.hyf.tx.other.pojo.Money;

/**
 * @author baB_hyf
 * @date 2020/04/05
 */
public interface MoneyServiceI {

	void transfer(String user, String target);

	void pyDeal();

	Money getMoney(Integer id);

	int insertMoney(Money money);

	int updateMoney(Money money);
}
