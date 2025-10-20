package po.dao;

import java.util.List;
import po.TopupLog;

public interface TopupLogDao {
	void insert(TopupLog log);

	List<TopupLog> findByUsername(String username); // 讀會員資金歷程
}
