package vttp2022.paf.assessment.eshop.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp2022.paf.assessment.eshop.exceptions.OrderException;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    // If anything fails, roll back database operations
    @Transactional
    public void createNewOrder (Order newOrder) throws OrderException{
        System.out.println("Creating New Order");
        // Save order to order table
        if (!orderRepository.saveOrder(newOrder)){
            throw new OrderException("Unable to create order");
        }

        // Save each line item into line_item table
    
       for (LineItem li : newOrder.getLineItems()){
        if (!orderRepository.saveLineItemToOrders(newOrder.getOrderId(), li)){
            throw new OrderException("Unable to create line item");
        }
       }
       System.out.println("Successfully Created Order: " + newOrder);
    }

    public Map<String,Integer> getOrderCounts(String name) {
        return orderRepository.getOrderCounts(name);
    }
}
