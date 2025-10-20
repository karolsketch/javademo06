package po.dao.impl;

import po.TopupLog;
import po.dao.TopupLogDao;
import util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopupLogDaoImpl implements TopupLogDao {
	@Override
	public void insert(TopupLog log) {
		String sql = "INSERT INTO topuplog(username, amount, balance, time) VALUES(?,?,?,?)";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, log.getUsername());
			ps.setBigDecimal(2, log.getAmount());
			ps.setBigDecimal(3, log.getBalance()); // ★ balance
			ps.setString(4, log.getTime());
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<TopupLog> findByUsername(String username) {
		String sql = "SELECT id, username, amount, balance, time FROM topuplog WHERE username=? ORDER BY id";
		List<TopupLog> list = new ArrayList<>();
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new TopupLog(rs.getInt("id"), rs.getString("username"), rs.getBigDecimal("amount"),
							rs.getBigDecimal("balance"), rs.getString("time")));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}
}
