package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.ItemRepository;
import com.menuit.menuitreplica.repository.OrderRepository;
import com.menuit.menuitreplica.repository.StoreRepository;
import com.menuit.menuitreplica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Transactional
    public Long createOrder(Long userId, Long storeId, String orderType, OrderItem... orderItems) throws IllegalAccessException {
        User user = userRepository.findOne(userId);
        Store store = storeRepository.findOne(storeId);

        //orderItems will be made at the controller, and send them to here.

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

    @Transactional
    public void cancelOrder(Long orderId) throws IllegalAccessException {
        Order order = orderRepository.findOne(orderId);

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

    @Transactional
    public void deleteOrderItemFromOrder(Long orderId, Long orderItemId) throws IllegalAccessException {
        Order order = orderRepository.findOne(orderId);
        OrderItem orderItem = orderRepository.findOneOrderItem(orderItemId);
        if(orderItem.getOrder() != order){
            throw new IllegalAccessException("This orderItem does not belong to the order.");
        }
        orderRepository.deleteOrderItemFromOrder(orderItemId);
    }

    public Order findOne(Long id) {
        return orderRepository.findOne(id);
    }

    public OrderItem findOneOrderItem(Long id){
        return orderRepository.findOneOrderItem(id);
    }

    public List<Order> findByUser(Long userId){
        User user = userRepository.findOne(userId);
        return orderRepository.findByUser(user);
    }

    public List<Order> findByStore(Long storeId){
        Store store = storeRepository.findOne(storeId);
        return orderRepository.findByStore(store);
    }

    public List<Order> findByItem(Long itemId){
//        Item item = itemRepository.findOne(itemId);
        return orderRepository.findByItem(itemId);
    }

    public List<Order> findByOrderType(String str){
        OrderType orderType = OrderType.valueOf(str);
        return orderRepository.findByOrderType(orderType);
    }

    public List<Order> findByOrderStatus(String str){
        OrderStatus orderStatus = OrderStatus.valueOf(str);
        return orderRepository.findByOrderStatus(orderStatus);
    }

    public List<Order> findOrdersBetweenTwoTimes(String start, String end){
        //As I will use html input 'datetime-local', the inputs will be like "1985-04-12T23:20"
        //which means there is no 'second' data.
        // make sure the seconds are set before parsing
        if (StringUtils.countOccurrencesOf(start, ":") == 1) {
            start += ":00";
        }
        if (StringUtils.countOccurrencesOf(end, ":") == 1) {
            end += ":00";
        }
        Timestamp startTime = Timestamp.valueOf(start.replace("T", " "));
        Timestamp endTime = Timestamp.valueOf(end.replace("T", " "));
        return orderRepository.findOrdersBetweenTwoTimes(startTime, endTime);
    }

    public List<Order> findByTotalAmount(double startAmount, double endAmount){
        if (startAmount>=endAmount){
            return orderRepository.findByTotalAmount(endAmount, startAmount);
        }
        return orderRepository.findByTotalAmount(startAmount, endAmount);
    }

    public List<Order> findByTotalTipAmount(double startAmount, double endAmount){
        if (startAmount>=endAmount){
            return orderRepository.findByTotalTipAmount(endAmount, startAmount);
        }
        return orderRepository.findByTotalTipAmount(startAmount, endAmount);
    }

    public List<Order> findByUserAndStore(Long userId, Long storeId){
        User user = userRepository.findOne(userId);
        Store store = storeRepository.findOne(storeId);
        return orderRepository.findByUserAndStore(user, store);
    }
}
