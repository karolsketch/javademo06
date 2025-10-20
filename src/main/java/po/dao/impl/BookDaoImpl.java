package po.dao.impl;

import po.Book;
import po.dao.BookDao;
import util.DbConnection;
import java.sql.*;
import java.util.*;

public class BookDaoImpl implements BookDao {
	public Book findById(int id) {
		String sql = "SELECT id,title,price,stock FROM book WHERE id=?";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Book(rs.getInt("id"), rs.getString("title"), rs.getBigDecimal("price"),
							rs.getInt("stock"));
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public java.util.List<Book> findAll() {
		String sql = "SELECT id,title,price,stock FROM book ORDER BY id";
		java.util.List<Book> list = new ArrayList<>();
		try (Connection con = DbConnection.getDb();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getBigDecimal("price"),
						rs.getInt("stock")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	public void decreaseStock(int id) {
		String sql = "UPDATE book SET stock=stock-1 WHERE id=? AND stock>0";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void increaseStock(int id) {
		String sql = "UPDATE book SET stock=stock+1 WHERE id=?";
		try (Connection con = DbConnection.getDb(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
