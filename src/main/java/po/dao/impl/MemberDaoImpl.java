package po.dao.impl;

import po.Member;
import po.dao.MemberDao;
import util.DbConnection;
import java.sql.*;
import java.math.BigDecimal;

public class MemberDaoImpl implements MemberDao {
	public Member findByUsernameAndPassword(String username, String password) {
		String sql = "SELECT id,name,username,password,balance FROM member WHERE username=? AND password=?";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Member m = new Member();
					m.setId(rs.getInt("id"));
					m.setName(rs.getString("name"));
					m.setUsername(rs.getString("username"));
					m.setPassword(rs.getString("password"));
					m.setBalance(rs.getBigDecimal("balance"));
					return m;
				}
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Member findByUsername(String username) {
		String sql = "SELECT id,name,username,password,balance FROM member WHERE username=?";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Member m = new Member();
					m.setId(rs.getInt("id"));
					m.setName(rs.getString("name"));
					m.setUsername(rs.getString("username"));
					m.setPassword(rs.getString("password"));
					m.setBalance(rs.getBigDecimal("balance"));
					return m;
				}
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void updateBalanceByUsername(String username, BigDecimal newBalance) {
		String sql = "UPDATE member SET balance=? WHERE username=?";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setBigDecimal(1, newBalance);
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
