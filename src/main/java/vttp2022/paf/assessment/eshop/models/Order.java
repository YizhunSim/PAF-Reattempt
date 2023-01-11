package vttp2022.paf.assessment.eshop.models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

// DO NOT CHANGE THIS CLASS
public class Order {

	private String orderId;
	private String deliveryId;
	private String name;
	private String address;
	private String email;
	private String status;
	private Date orderDate = new Date();
	private List<LineItem> lineItems = new LinkedList<>();

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDeliveryId() {
		return this.deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Customer getCustomer() {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setAddress(address);
		customer.setEmail(email);
		return customer;
	}

	public void setCustomer(Customer customer) {
		name = customer.getName();
		address = customer.getAddress();
		email = customer.getEmail();
	}

	public List<LineItem> getLineItems() {
		return this.lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public void addLineItem(LineItem lineItem) {
		this.lineItems.add(lineItem);
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", deliveryId=" + deliveryId + ", name=" + name + ", address=" + address
				+ ", email=" + email + ", status=" + status + ", orderDate=" + orderDate + ", lineItems=" + lineItems
				+ "]";
	}

	public JsonObject toJson(String name) {
		// Convert line items into list of jsonObjects
		List<JsonObject> lineItemsJsonList = this.lineItems.stream().map(li -> li.toJson()).toList();

		// Add line item Json Objects into JsonArray
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (JsonObject jo : lineItemsJsonList) {
			arrBuilder.add(jo);
		}

		// Build the JsonArray of line Items
		JsonArray lineItemsArray = arrBuilder.build();

		JsonObject json = Json.createObjectBuilder()
				.add("orderId", this.getOrderId())
				.add("name", this.getName())
				.add("address", this.getAddress())
				.add("email", this.getEmail())
				.add("lineItems", lineItemsArray)
				.add("createdBy", name)
				.build();
		return json;
	}

}
