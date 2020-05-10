package com.hyf.tx.other.pojo;

/**
 * drop table if exists spring_tx_test_money;
 * create table spring_tx_test_money(
 *   id int primary key not null auto_increment,
 *   money int(10),
 *   unit varchar(10)
 * );
 *
 * insert into spring_tx_test_money (money,unit) values (1,'元');
 * select * from spring_tx_test_money;
 *
 * @author baB_hyf
 * @date 2020/04/05
 */
public class Money {

	private Integer id;

	private Integer number;

	private String unit = "元";

	public Money() {
	}

	public Money(Integer number) {
		this.number = number;
	}

	public Money(Integer id, Integer number) {
		this.id = id;
		this.number = number;
	}

	public Money(Integer id, Integer number, String unit) {
		this.id = id;
		this.number = number;
		this.unit = unit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "Money{" +
				"id=" + id +
				", number=" + number +
				", unit='" + unit + '\'' +
				'}';
	}
}
