package vttp2022.paf.assessment.eshop.services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.paf.assessment.eshop.exceptions.OrderException;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.OrderStatusRepository;

@Service
public class WarehouseService {
	// Post order to paf.chukless.com/dispatch
	private final String WAREHOUSE_URL = "http://paf.chuklee.com/dispatch";

	private final String NAME_NRIC = "SIM YIZHUN";

	private final String DISPATCHED = "dispatched";

	private final String PENDING = "pending";

	@Autowired
	private OrderStatusRepository orderStatusRepository;

	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) {
		try {
	
			// build the url
			String dispatchURL = UriComponentsBuilder
					.fromUriString(WAREHOUSE_URL)
					.path("/" + order.getOrderId())
					.toUriString();

			System.out.println("Dispatch URL: " + dispatchURL);

			JsonObject requestBody = order.toJson(NAME_NRIC);

			RequestEntity<String> request = RequestEntity
					.post(dispatchURL)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.body(requestBody.toString());

			// Send Request
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(request, String.class);

			OrderStatus orderStatus = new OrderStatus();

			System.out.println("Response from Server: " + response.getBody());
			JsonReader reader = Json.createReader(new StringReader(response.getBody()));
			JsonObject orderStatusJson = reader.readObject();

			orderStatus.setOrderId(orderStatusJson.getString("orderId"));
			orderStatus.setDeliveryId(orderStatusJson.getString("deliveryId"));
			orderStatus.setStatus(DISPATCHED);

			// Save order status (Dispatch success / Dispatch unsuccessful)
			if (orderStatusRepository.saveOrderStatus(orderStatus)) {
				System.out.println("Successful saved order status table: " + orderStatus);
			} else {
				System.out.println("Failed to save to order status table: " + orderStatus);
			}
			return orderStatus;
		} catch (HttpServerErrorException ex) {
			System.err.println(ex.getMessage());

			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setOrderId(order.getOrderId());
			orderStatus.setStatus(PENDING);

			// Save order status (Dispatch success / Dispatch unsuccessful)
			if (orderStatusRepository.saveOrderStatus(orderStatus)) {
				System.out.println("Successful saved order status table: " + orderStatus);
			} else {
				System.out.println("Failed to save to order status table: " + orderStatus);
			}
			return orderStatus;
		}
		

	}
}
