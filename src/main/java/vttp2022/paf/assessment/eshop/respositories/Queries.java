package vttp2022.paf.assessment.eshop.respositories;

public class Queries {
    public static final String SQL_SELECT_CUSTOMER_BY_NAME = "SELECT * FROM customers WHERE name = ?;";

    public static final String SQL_INSERT_ORDERS = "INSERT INTO orders (order_id, name) values (?, ?);";

    public static final String SQL_INSERT_LINE_ITEMS = "INSERT INTO line_items (order_id, item, quantity) values (?, ?, ?);";

    public static final String SQL_INSERT_ORDER_STATUS = "INSERT INTO order_status (order_id, delivery_id, status, status_update) values (?, ?, ?, ?);";

    public static final String SQL_SELECT_ORDER_COUNT_BY_NAME = "SELECT status, count(*) as count FROM order_status os JOIN orders o ON os.order_id = o.order_id WHERE name = ? group by status;";
}
