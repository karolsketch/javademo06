package po;

import java.io.Serializable;
import java.math.BigDecimal;

public class Member implements Serializable {
	private Integer id;
	private String name;
	private String username;
	private String password;
	private BigDecimal balance;

	public Member() {
	}

	public Member(Integer id, String name, String username, String password, BigDecimal balance) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
