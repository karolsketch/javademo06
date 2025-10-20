package po.dao;

import po.Book;
import java.util.List;

public interface BookDao {
	Book findById(int id);

	java.util.List<Book> findAll();

	void decreaseStock(int id);

	void increaseStock(int id);
}
