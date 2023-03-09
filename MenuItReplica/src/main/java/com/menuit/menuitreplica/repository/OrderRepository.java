package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public Long createOrder(Order order){
//        Store store = order.getStore();
//        User user = order.getUser();
//        List<Order> byUserAndStore = findByUserAndStore(user, store);
//        for(Order pastOrder: byUserAndStore){
//            long orderedTime = pastOrder.getOrderTime().getTime();
//            long now = Timestamp.valueOf(LocalDateTime.now()).getTime();
//            long diffInSeconds = Math.abs(TimeUnit.MICROSECONDS.toSeconds(now - orderedTime));
//            // order made by same user to same store is exist in last 3 mins,
//            // and it is not cancelled, throw an error.
//            if(diffInSeconds<180 && pastOrder.getOrderStatus() != OrderStatus.cancelled){
//                throw new DuplicationOfOrderException();
//            }
//        }
        if(order.getId() != null){
            if(Objects.equals(order.getId(), findOne(order.getId()).getId())){
                em.merge(order);
            }
        } else{
            em.persist(order);
        }
        return order.getId();
    }

    public void cancelOrder(Order order){
        order.cancelOrder();
    }

    public void deleteOrderItemFromOrder(Long orderItemId){
        OrderItem orderItem = findOneOrderItem(orderItemId);
        orderItem.getOrder().getOrderItems().remove(orderItem);

        //set the payment again to update orderItems list in payment/payer
        if(orderItem.getOrder().getPayment().isGratuity()){
            Payment.createPayment(orderItem.getOrder(),100);
        } else{
            Payment.createPayment(orderItem.getOrder(),1);
        }
    }


    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public OrderItem findOneOrderItem(Long id){
        return em.find(OrderItem.class, id);
    }

    public List<Order> findByUser(User user){
        return em.createQuery("select o from Order o where o.user = :user", Order.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Order> findByStore(Store store){
        return em.createQuery("select o from Order o where o.store = :store", Order.class)
                .setParameter("store", store)
                .getResultList();
    }

    public List<Order> findByItem(Long itemId){
//        List<OrderItem> orderItemList = em.createQuery("select oi from OrderItem oi where oi.item = :item", OrderItem.class)
//                .setParameter("item", item)
//                .getResultList();
//        List<Order> orderList = new ArrayList<>();
//        for(OrderItem orderItem: orderItemList){
//            if(!orderList.contains(orderItem.getOrder())){
//                orderList.add(orderItem.getOrder());
//            }
//        }
        List<Order> orderList = em.createQuery("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi JOIN oi.item i WHERE i.id = :itemId", Order.class)
                .setParameter("itemId", itemId)
                .getResultList();
        System.out.println("find orders that contain orderItems with an item that matches with given itemId: "+ itemId);
        for(Order order: orderList){
            System.out.println("Order Id: "+ order.getId());
            for(Payer payer: order.getPayment().getPayers()){
                for(OrderItem orderItem: payer.getOrderItems()){
                    System.out.println("Item Id: "+ orderItem.getItem().getId());
                }
            }
        }
        System.out.println("size of result list: " + orderList.size());
        return orderList;

//        return orderList;
//        return em.createQuery("select o from Order o JOIN FETCH o.orderItems where o.orderItems")
    }

    public List<Order> findByOrderType(OrderType orderType){
        return em.createQuery("select o from Order o where o.orderType = :orderType", Order.class)
                .setParameter("orderType", orderType)
                .getResultList();
    }
    public List<Order> findByOrderStatus(OrderStatus orderStatus){
        return em.createQuery("select o from Order o where o.orderStatus = :orderStatus", Order.class)
                .setParameter("orderStatus", orderStatus)
                .getResultList();
    }
    public List<Order> findOrdersBetweenTwoTimes(Timestamp startTime, Timestamp endTime){
        return em.createQuery("select o from Order o where o.orderTime between :startTime and :endTime", Order.class)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .getResultList();
    }
    public List<Order> findByTotalAmount(double startAmount, double endAmount){
        return em.createQuery("select o from Order o where o.payment.total between :startAmount and :endAmount", Order.class)
                .setParameter("startAmount", startAmount)
                .setParameter("endAmount", endAmount)
                .getResultList();
    }

    public List<Order> findByTotalTipAmount(double startAmount, double endAmount){
        //between keyword includes both ends
        return em.createQuery("select o from Order o where o.payment.totalTipAmount between :startAmount and :endAmount", Order.class)
                .setParameter("startAmount", startAmount)
                .setParameter("endAmount", endAmount)
                .getResultList();
    }

    public List<Order> findByUserAndStore(User user, Store store){
//        LocalDateTime time = LocalDateTime.now();
//        루프돌려서 now-order.time < 10mins 인것만 다시 리턴

//        return em.createQuery("select o from Order o where o.user = :user and o.store = :store and o.orderTime = :time", Order.class)
        return em.createQuery("select o from Order o where o.user = :user and o.store = :store", Order.class)
                .setParameter("user", user)
                .setParameter("store", store)
//                .setParameter("time", time)
                .getResultList();
    }

}
