package po.dao.impl;

import po.RentLog;
import po.dao.RentLogDao;
import util.DbConnection;
import java.sql.*;

public class RentLogDaoImpl implements RentLogDao {
	public void insert(RentLog log) {
		String sql = "INSERT INTO rentlog(username,bookid,action,time) VALUES(?,?,?,?)";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, log.getUsername());
			ps.setInt(2, log.getBookid());
			ps.setString(3, log.getAction());
			ps.setString(4, log.getTime());
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
