package po.dao;

import po.Member;
import java.math.BigDecimal;

public interface MemberDao {
	Member findByUsernameAndPassword(String username, String password);

	Member findByUsername(String username);

	void updateBalanceByUsername(String username, BigDecimal newBalance);
}
