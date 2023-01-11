package vttp2022.paf.assessment.eshop.controllers;

import java.io.StringReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp2022.paf.assessment.eshop.exceptions.OrderException;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;
import vttp2022.paf.assessment.eshop.services.CustomerService;
import vttp2022.paf.assessment.eshop.services.OrderService;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

@RestController
public class OrderController {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private WarehouseService warehouseService;

	// TODO: Task 3
	@PostMapping(path = "/api/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveOrder(@RequestBody String payload) {

		/*
		 * Taken from Request Payload
		 * {"name":"fred",
		 * "lineItems":[
		 * {"item":"phone","quantity":1},
		 * {"item":"laptop","quantity":1}
		 * ]
		 * }
		 * 
		 */
		try {
			System.out.println("Checkout");
			JsonReader reader = Json.createReader(new StringReader(payload));
			JsonObject orderJson = reader.readObject();
			String name = orderJson.getString("name");

			System.out.println("OrderJson %s".formatted(orderJson));
			System.out.println("name: %s".formatted(name));

			Optional<Customer> customerResult = customerService.findCustomerByName(name);
			if (customerResult.isEmpty()) {
				// Build Error Reponse
				JsonObjectBuilder errorResponseBuilder = Json.createObjectBuilder();
				JsonObject errorReponseObject = errorResponseBuilder
						.add("error", "Customer %s not found".formatted(name))
						.build();

				return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.contentType(MediaType.APPLICATION_JSON)
						.body(errorReponseObject.toString());
			}

			/*
			 * private String orderId;
			 * private String deliveryId;
			 * private String name;
			 * private String address;
			 * private String email;
			 * private String status;
			 * private Date orderDate = new Date();
			 * private List<LineItem> lineItems = new LinkedList<>();
			 */

			Order orderToBeInserted = new Order();
			orderToBeInserted.setOrderId(UUID.randomUUID().toString().substring(0, 8));
			orderToBeInserted.setName(name);
			orderToBeInserted.setAddress(customerResult.get().getAddress());
			orderToBeInserted.setEmail(customerResult.get().getEmail());
			orderToBeInserted.setStatus("pending");
			orderToBeInserted.setOrderDate(new Date());

			List<LineItem> listOfLineItems = new LinkedList<>();

			// Create JsonValues in array to line items
			JsonArray lineItemsArray = orderJson.getJsonArray("lineItems");

			for (JsonValue jv : lineItemsArray) {
				JsonObject jo = (JsonObject) jv;
				LineItem li = LineItem.fromJson(jo);
				listOfLineItems.add(li);
			}

			orderToBeInserted.setLineItems(listOfLineItems);

			// Save the new order - Throws OrderException if error occured
			orderService.createNewOrder(orderToBeInserted);

			// Dispatch the order
			OrderStatus orderStatusResult = warehouseService.dispatch(orderToBeInserted);
			JsonObjectBuilder jsonStatusBuilder = Json.createObjectBuilder();

			if (orderStatusResult.getStatus().equals("dispatched")) {
				jsonStatusBuilder
						.add("orderId", orderStatusResult.getOrderId())
						.add("deliveryId", orderStatusResult.getDeliveryId())
						.add("status", orderStatusResult.getStatus());
			} else if (orderStatusResult.getStatus().equals("pending")) {
				jsonStatusBuilder
						.add("orderId", orderStatusResult.getOrderId())
						.add("status", orderStatusResult.getStatus());
			}
			return ResponseEntity
					.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(jsonStatusBuilder.build().toString());

		} catch (OrderException ex) {
			ex.printStackTrace();
			JsonObjectBuilder errorResponseBuilder = Json.createObjectBuilder();
			JsonObject errorReponseObject = errorResponseBuilder
					.add("error", ex.getMessage())
					.build();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(MediaType.APPLICATION_JSON)
					.body(errorReponseObject.toString());
		}

	}

	@GetMapping(path = "/api/order/{name}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getOrderCounts (@PathVariable String name){
		
		/*
		 * {
		 * 	"name": fred,
		 * 	"dispatched": 10,
		 *  "pending": 20
		 * }
		 */
		Map<String,Integer> orderCount = orderService.getOrderCounts(name);
		JsonObject json = Json.createObjectBuilder()
							.add("name", name)
							.add("dispatched", orderCount.get("dispatched"))
							.add("pending", orderCount.get("pending"))
							.build();
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(json.toString());
	}

}
