package com.hyf.tx.other.service.impl;


import com.hyf.tx.other.pojo.Money;
import com.hyf.tx.other.service.MoneyServiceI;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author baB_hyf
 * @date 2020/04/05
 */
@Service
public class MoneyServiceImpl implements MoneyServiceI {

	@Autowired
	private DataSource dataSource;


	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void transfer(String user, String target) {

		insertMoney(new Money(1000));
//		int i = 1/0;
		insertMoney(new Money(2000));
		// just print
		System.out.println(String.format("[%s] -> [%s]   transfer success!!!", user, target));
	}

	@Transactional
	@Override
	public void pyDeal() {
		System.out.println(1);
	}











	//---------------------------------------------------------------------
	// business logic
	//---------------------------------------------------------------------


	@Override
	public Money getMoney(Integer id) {
		try {
			Connection conn = DataSourceUtils.getConnection(dataSource);
			PreparedStatement pstmt = conn.prepareStatement("select * from spring_tx_test_money where id = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer idNum = rs.getInt("id");
				Integer moneyNum = rs.getInt("money");
				String unitNum = rs.getString("unit");
				return new Money(idNum, moneyNum, unitNum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// omit close method...
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int insertMoney(Money money) {
		try {
			Connection conn = DataSourceUtils.getConnection(dataSource);
			PreparedStatement pstmt = conn.prepareStatement("insert into spring_tx_test_money (money, unit) values (?, ?)");
			pstmt.setInt(1, money.getNumber());
			pstmt.setString(2, money.getUnit());
			// omit close method...
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateMoney(Money money) {
		// omit because lazy...
		throw new UnsupportedOperationException(money.toString());
	}


	//---------------------------------------------------------------------
	// inject dependency
	//---------------------------------------------------------------------


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
