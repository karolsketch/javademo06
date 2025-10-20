package po;

import java.io.Serializable;

public class RentLog implements Serializable {
	private Integer id;
	private String username;
	private Integer bookid;
	private String action;
	private String time;

	public RentLog() {
	}

	public RentLog(Integer id, String username, Integer bookid, String action, String time) {
		this.id = id;
		this.username = username;
		this.bookid = bookid;
		this.action = action;
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

	public Integer getBookid() {
		return bookid;
	}

	public void setBookid(Integer bookid) {
		this.bookid = bookid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
