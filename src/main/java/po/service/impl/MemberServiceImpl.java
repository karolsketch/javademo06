package po.service.impl;

import exception.AppException;
import po.*;
import po.dao.*;
import po.dao.impl.*;

public class MemberServiceImpl implements po.service.MemberService {
	private final MemberDao memberDao = new MemberDaoImpl();
	private final BookDao bookDao = new BookDaoImpl();
	private final RentLogDao rentLogDao = new RentLogDaoImpl();
	private final TopupLogDao topupLogDao = new TopupLogDaoImpl();
	private static final java.time.format.DateTimeFormatter FMT = java.time.format.DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss");

	public Member login(String username, String password) {
		Member m = memberDao.findByUsernameAndPassword(username, password);
		if (m == null)
			throw new AppException("帳號或密碼錯誤");
		return m;
	}

	public void topup(String username, java.math.BigDecimal amount) {
		if (amount.compareTo(java.math.BigDecimal.ZERO) <= 0)
			throw new AppException("儲值金額需大於0");
		Member m = memberDao.findByUsername(username);
		if (m == null)
			throw new AppException("會員不存在");
		java.math.BigDecimal newBal = m.getBalance().add(amount);
		memberDao.updateBalanceByUsername(username, newBal);
		String now = java.time.LocalDateTime.now().format(FMT);
		topupLogDao.insert(new TopupLog(null, username, amount, newBal, now));
	}

	public void rent(String username, int bookId) {
		Member m = memberDao.findByUsername(username);
		Book b = bookDao.findById(bookId);
		if (m == null || b == null)
			throw new AppException("會員或書籍不存在");
		if (b.getStock() <= 0)
			throw new AppException("庫存不足");
		if (m.getBalance().compareTo(b.getPrice()) < 0)
			throw new AppException("餘額不足，請先儲值");

		java.math.BigDecimal newBal = m.getBalance().subtract(b.getPrice());
		memberDao.updateBalanceByUsername(username, newBal);
		bookDao.decreaseStock(bookId);

		String now = java.time.LocalDateTime.now().format(FMT);
		rentLogDao.insert(new RentLog(null, username, bookId, "rent", now));

		// ★ 資金流：租書記一筆「負金額」
		topupLogDao.insert(new TopupLog(null, username, b.getPrice().negate(), newBal, now));
	}

	public void returnBook(String username, int bookId) {
		Book b = bookDao.findById(bookId);
		if (b == null)
			throw new AppException("書籍不存在");
		bookDao.increaseStock(bookId);
		String now = java.time.LocalDateTime.now().format(FMT);
		rentLogDao.insert(new RentLog(null, username, bookId, "return", now));
		// 還書不變更餘額，因此不寫 topuplog
	}

	public java.util.List<TopupLog> getTopupLogs(String username) {
		return topupLogDao.findByUsername(username);
	}
}
