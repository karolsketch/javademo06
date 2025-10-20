package po.service;

import java.math.BigDecimal;
import java.util.List;
import po.Member;
import po.TopupLog;

public interface MemberService {
	Member login(String username, String password);

	void topup(String username, BigDecimal amount); // amount>0；寫 topuplog(balance 為交易後)

	void rent(String username, int bookId); // 寫 rentlog；topuplog 寫負金額＋交易後餘額

	void returnBook(String username, int bookId); // 寫 rentlog

	List<TopupLog> getTopupLogs(String username); // 查歷程
}
