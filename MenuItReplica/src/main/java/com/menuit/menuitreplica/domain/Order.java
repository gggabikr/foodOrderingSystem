package com.menuit.menuitreplica.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

//    private double tipAmount;

    @Enumerated(EnumType.STRING)
    @NotEmpty
    private OrderType orderType; //table, pickUp, delivery

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //confirming, preparing, orderReady, completed, paidInFull, cancelled

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, insertable=false, updatable=false)
    private Timestamp orderTime;

    private LocalDateTime scheduledPickUpTime;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    //==Constructor==//
    public static Order createTableOrder(
            User user, Store store,OrderItem... orderItems){
        Order order = new Order();
        order.setUser(user);
        order.setStore(store);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.orderType = OrderType.table;
        order.orderStatus = OrderStatus.preparing;
        return order;
    }

    public static Order createNonTableOrder(
            User user, Store store,
            OrderType orderType, OrderItem... orderItems){
        Order order = new Order();
        order.setUser(user);
        order.setStore(store);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.orderType = orderType;
        order.orderStatus = OrderStatus.confirming;
        return order;
    }

    //==Relational methods==//
    public void setUser(User user){
        this.user = user;
        user.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        this.getOrderItems().add(orderItem);
        orderItem.setOrder(this);
    }

    public void setStore(Store store){
        this.store = store;
        store.getOrders().add(this);
    }

//    //==Business methods==//
//    public double getSubtotal(){
//        double subtotal = 0;
//        for(OrderItem orderItem: getOrderItems()){
//            double subtotalEach = orderItem.getOrderPrice() * orderItem.getCount();
//            subtotal +=subtotalEach;
//        }
//        return Math.round(subtotal * 100) / 100.0;
//    }
//
//    public double getGST(){
//        return Math.round(getSubtotal() * 5)/100.0;
//    }
//
//    public double getPST(){
//        double alcoholic = 0;
//        double soda = 0;
//        for(OrderItem orderItem: getOrderItems()){
//            double subtotalEach = orderItem.getOrderPrice() * orderItem.getCount();
//            ItemType itemType = orderItem.getItem().getItemType();
//
//            if(itemType.equals(ItemType.alcoholic)){
//                alcoholic += subtotalEach;
//            } else if(itemType.equals(ItemType.soda)){
//                soda += subtotalEach;
//            }
//        }
//        double PstBeforeRounding = alcoholic * 0.1 + soda * 0.07;
//        return Math.round(PstBeforeRounding*100)/100.0;
//    }
//
//    public double getTotal(){
//        return Math.round((getSubtotal()+getGST()+getPST())*100)/100.0;
//    }
}

