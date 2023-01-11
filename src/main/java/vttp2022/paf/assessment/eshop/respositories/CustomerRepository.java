package vttp2022.paf.assessment.eshop.respositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

import vttp2022.paf.assessment.eshop.models.Customer;

@Repository
public class CustomerRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	// You cannot change the method's signature
	public Optional<Customer> findCustomerByName(String name) {
		// TODO: Task 3
		SqlRowSet result = jdbcTemplate.queryForRowSet(SQL_SELECT_CUSTOMER_BY_NAME, name);

		if(!result.next()){
			return Optional.empty();
		}else{
			return Optional.of(Customer.create(result));
		}
	}
}
