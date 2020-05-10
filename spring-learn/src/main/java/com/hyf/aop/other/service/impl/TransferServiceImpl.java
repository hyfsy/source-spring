package com.hyf.aop.other.service.impl;

import com.hyf.aop.other.service.TransferServiceI;
import org.springframework.stereotype.Service;

/**
 * @author baB_hyf
 * @date 2020/03/30
 */
@Service("transferService")
public class TransferServiceImpl implements TransferServiceI {

	@Override
	public void transfer() {
		System.out.println("transfer success!!!");
	}

	@Override
	public void transferWithArgs(String asdf) {
		System.out.println("transfer with args: " + asdf);
	}

	@Override
	public void transferWithEx() {
		System.out.println("start transfer");

		int i = 1/0;

		System.out.println("end transfer");
	}

}
