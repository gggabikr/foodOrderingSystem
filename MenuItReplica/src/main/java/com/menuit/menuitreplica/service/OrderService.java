package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.ItemRepository;
import com.menuit.menuitreplica.repository.OrderRepository;
import com.menuit.menuitreplica.repository.StoreRepository;
import com.menuit.menuitreplica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;


    public Long createOrder(Long userId, Long storeId, String orderType,OrderItem... orderItems){
        User user = userRepository.findOne(userId);
        Store store = storeRepository.findOne(storeId);
        OrderType orderType1 = OrderType.valueOf(orderType);
        Order order;

        if(orderType1.equals(OrderType.table)){
            order = Order.createTableOrder(user, store, orderItems);
        } else{
            order = Order.createNonTableOrder(user, store, orderType1, orderItems);
        }
        orderRepository.createOrder(order);
        return order.getId();
    };

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

    public void cancelOrder(Order order){
        //change order status;
    }
}
