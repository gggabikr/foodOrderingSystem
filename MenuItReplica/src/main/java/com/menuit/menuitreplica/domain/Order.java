package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
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

    private List<OrderItem> orderItems = new ArrayList<>();

    private float tipAmount;

    private OrderType orderType; //table, pickUp, delivery

    private OrderStatus orderStatus; //confirming, preparing, orderReady, completed, paidInFull, cancelled

    private LocalDateTime orderTime;

    private DateTimeFormatter scheduledPickUpTime;

    private String tableName; //only for the table order
}
