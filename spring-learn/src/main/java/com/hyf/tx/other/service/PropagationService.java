package com.hyf.tx.other.service;

import com.hyf.tx.other.pojo.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author baB_hyf
 * @date 2020/04/11
 */
@Component
public class PropagationService {

	@Autowired
	private MoneyServiceI moneyService;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Transactional(propagation = Propagation.REQUIRED)
	public void testSupports() {
		moneyService.insertMoney(new Money(1000));
		moneyService.insertMoney(new Money(2000));
	}

	public void testProgrammingTx() {
		transactionTemplate.execute((status) -> {
			String a = "1";
			return "success";
		});
	}


}
