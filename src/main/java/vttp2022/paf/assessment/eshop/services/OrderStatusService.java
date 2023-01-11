package vttp2022.paf.assessment.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.OrderStatusRepository;

@Service
public class OrderStatusService {
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public boolean saveOrderStatus(OrderStatus os){
        return orderStatusRepository.saveOrderStatus(os);
    }
}
