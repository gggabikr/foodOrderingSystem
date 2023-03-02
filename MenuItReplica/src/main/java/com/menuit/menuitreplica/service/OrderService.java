package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order findOne(Long id) {
        return orderRepository.findOne(id);
    }

    public List<Order> findByUser(User user){
        return orderRepository.findByUser(user);
    }

    public List<Order> findByStore(Store store){
        return orderRepository.findByStore(store);
    }

    public List<Order> findByItem(Item item){
        return orderRepository.findByItem(item);
    }

    public List<Order> findByOrderType(OrderType orderType){
        return orderRepository.findByOrderType(orderType);
    }
    public List<Order> findByOrderStatus(OrderStatus orderStatus){
        return orderRepository.findByOrderStatus(orderStatus);
    }
    public List<Order> findOrdersBetweenTwoTimes(Timestamp startTime, Timestamp endTime){
        return orderRepository.findOrdersBetweenTwoTimes(startTime, endTime);
    }
    public List<Order> findByTotalAmount(double startAmount, double endAmount){
        return orderRepository.findByTotalAmount(startAmount, endAmount);
    }

    public List<Order> findByTotalTipAmount(double startAmount, double endAmount){
        return orderRepository.findByTotalTipAmount(startAmount, endAmount);
    }

    public List<Order> findByUserAndStore(User user, Store store){
        return orderRepository.findByUserAndStore(user, store);
    }
}
