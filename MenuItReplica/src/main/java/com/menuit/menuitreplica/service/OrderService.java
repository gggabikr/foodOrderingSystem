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


    public Long createOrder(Long userId, Long storeId, String orderType,OrderItem... orderItems) throws IllegalAccessException {
        User user = userRepository.findOne(userId);
        Store store = storeRepository.findOne(storeId);
        OrderType orderType1 = OrderType.valueOf(orderType);
        for(OrderItem orderItem : orderItems){
            if(!orderItem.getItem().getStore().equals(store)){
                throw new IllegalArgumentException("All the items need to be of same store.");
            }
        }
        Order order;

        if(orderType1.equals(OrderType.table)){
            if(!user.getRole().equals(UserRole.ROLE_TABLE)){
                throw new IllegalAccessException("Non-table user cannot make table order.");
            }
            order = Order.createTableOrder(user, store, orderItems);
        } else{
            if(user.getRole().equals(UserRole.ROLE_TABLE)){
                throw new IllegalAccessException("Table user cannot make non-table order.");
            }
            order = Order.createNonTableOrder(user, store, orderType1, orderItems);
        }
        orderRepository.createOrder(order);
        return order.getId();
    }

    public void cancelOrder(Order order) throws IllegalAccessException {
        boolean noPaymentExist = true;
        for(Payer payer : order.getPayment().getPayers()){
            if (payer.isPaid()) {
                noPaymentExist = false;
                break;
            }
        }
        if(order.getPayment().isStatus() || !noPaymentExist){
            throw new IllegalAccessException("Order cannot be cancelled unless no payment has made or all these are refunded.");
        } else{
            //change order status;
            orderRepository.cancelOrder(order);
        }
    }

    public void deleteOrderItemFromOrder(Long orderItemId){
        orderRepository.deleteOrderItemFromOrder(orderItemId);
    }

    public Order findOne(Long id) {
        return orderRepository.findOne(id);
    }

    public OrderItem findOneOrderItem(Long id){
        return orderRepository.findOneOrderItem(id);
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
