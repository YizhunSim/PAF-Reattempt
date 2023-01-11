package vttp2022.paf.assessment.eshop.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	// TODO: Task 3
	public boolean saveOrder (Order newOrder){
		System.out.println("Saving Order: " + newOrder);
		return jdbcTemplate.update(SQL_INSERT_ORDERS, newOrder.getOrderId(), newOrder.getName()) > 0;
	}
	
	public boolean saveLineItemToOrders (String orderId, LineItem li){
		System.out.println("Saving Line Item To Order Id: " + orderId + li);
        return jdbcTemplate.update(SQL_INSERT_LINE_ITEMS, orderId, li.getItem(), li.getQuantity()) > 0;
    }

	public Map<String,Integer> getOrderCounts(String name) {
		final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ORDER_COUNT_BY_NAME, name);
		/*
		 * {	
		 * 	"dispatched": 10,
		 * 	"pending": 5
		 * }
		 */

		 /*
		  *	| ------------------|
		  *	|status		| count	|
		  * |dispatched |   10  |
		  * |pending    |   5   |
		  */
		// Insert Row values
		Map<String, Integer> statusCountMap = new HashMap<>();
		while(rs.next()){
			statusCountMap.put(rs.getString("status"), rs.getInt("count"));
		}
		return statusCountMap;
	}

}
