package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Entity
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    private User user;

    private Store store;

    private ArrayList<OrderItem> orderItems;

    private float tipAmount;

    private OrderType orderType; //table, pickUp, delivery

    private OrderStatus orderStatus; //confirming, preparing, orderReady, completed, paidInFull, cancelled

    private LocalDateTime orderTime;

    private DateTimeFormatter scheduledPickUpTime;

    private String tableName; //only for the table order
}
