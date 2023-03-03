package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem implements Cloneable{
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private double orderPrice; //주문 가격 per unit

    private int count; //주문 수량

    private double discountPerItem; //개당 할인금액

    private String comment; //기타 메모

    //==Constructor==//
    public static OrderItem createOrderItem(Item item, int count){
        return createOrderItem(item, count, "");
    }

    public static OrderItem createOrderItem(Item item, int count, String comment){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        if(item.getDiscountPercent() == 0 && item.getDiscountAmount() == 0){
            orderItem.setOrderPrice(roundWithTwoDecimals(item.getPrice()));
        } else if(item.getDiscountAmount() != 0){
            orderItem.setOrderPrice(roundWithTwoDecimals(item.getPrice() - item.getDiscountAmount()));
            orderItem.setDiscountPerItem(item.getDiscountAmount());
        } else{
            orderItem.setOrderPrice(roundWithTwoDecimals(item.getPrice() * (1-item.getDiscountPercent())));
            orderItem.setDiscountPerItem(roundWithTwoDecimals(item.getPrice()-(item.getPrice() * (1-item.getDiscountPercent()))));
        }
        orderItem.setCount(count);
        orderItem.setComment(comment);

        return orderItem;
    }

    public static double roundWithTwoDecimals(double number){
        return Math.round(number*100)/100.0;
    }

    public double getTotalPrice(){
        return roundWithTwoDecimals(getOrderPrice()*getCount());
    }

    @Override
    public OrderItem clone() {
        try {
            OrderItem clone = (OrderItem) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.setItem(this.getItem());
            clone.setOrderPrice(this.getOrderPrice());
            clone.setComment(this.getComment());
            clone.setDiscountPerItem(this.getDiscountPerItem());
            clone.setCount(this.count);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
