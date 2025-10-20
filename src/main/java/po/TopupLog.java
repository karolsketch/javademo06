package po;

import java.io.Serializable;
import java.math.BigDecimal;

/** 儲值/扣款紀錄：amount 正=儲值、負=租書；balance=交易後餘額；time 為字串 */
public class TopupLog implements Serializable {
	private Integer id;
	private String username;
	private BigDecimal amount;
	private BigDecimal balance; // ★ 新增：交易後餘額
	private String time; // yyyy-MM-dd HH:mm:ss

	public TopupLog() {
	}

	public TopupLog(Integer id, String username, BigDecimal amount, BigDecimal balance, String time) {
		this.id = id;
		this.username = username;
		this.amount = amount;
		this.balance = balance;
		this.time = time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
