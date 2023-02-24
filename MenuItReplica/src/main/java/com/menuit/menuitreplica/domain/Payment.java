package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Payment {
    @Id @GeneratedValue
    @Column(name="payment_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private double subtotal;

    private double GST;

    private double PST;

    private double totalTipAmount;

    private boolean gratuity;

    //payer이고 뭐고 다 때려치고 스트링으로 영수증 만들어내는거나 만들까 생각중.

    @OneToMany
    private List<Payer> payers = new ArrayList<>();

//    private Store store;

    public void makeEvenPayments(int numOfCustomers){
        for(int i=1; i<numOfCustomers+1; i++){
            Payer payer = new Payer();
            payer.setName("Customer " + i);
            payer.setPaid(false);
            payers.add(payer);
        }
        for(Payer payer: payers){
            for(OrderItem orderItem: getOrder().getOrderItems()){
                payer.getOrderItems().add(divideOrderItem(orderItem, numOfCustomers));
            }
        }
    }



//    public void makeEvenPayments(int numOfCustomers){
//        List<Payer> customers = new ArrayList<>();
//        for(int i=1; i<numOfCustomers+1; i++){
//            Payer payer = new Payer();
//            payer.setName("Customer " + i);
//            customers.add(payer);
//        }
//        this.payers = customers;
//
//        for(Payer payer: customers){
//            for(OrderItem orderItem: getOrder().getOrderItems()){
//                payer.getOrderItems().add(divideOrderItem(orderItem, numOfCustomers));
//            }
//        }
//        getOrder().getOrderItems().clear();
//        getOrder().getOrderItems().
//
//    }

//    public void makeOneMorePayer(){
//        Payer payer = new Payer();
//        payer.setName("Customer " + getPayers().size()+1);
//        this.payers.add(payer);
//    }
//
//    public void deleteLastPayer(){
//        getPayers().remove(getPayers().size());
//    }

    public OrderItem divideOrderItem(OrderItem orderItem, int num){
//        double dividedOrderItem = Math.round((orderItem.getTotalPrice() / num)*100)/100.0;
        OrderItem dividedOrderItem = new OrderItem();
        dividedOrderItem.setItem(orderItem.getItem());
        dividedOrderItem.setOrderPrice(Math.round((orderItem.getOrderPrice()/num)*100)/100.0);
        dividedOrderItem.setCount(1);

//        for (Payer payer:payers) {
//            payer.getOrderItems().add(dividedOrderItem);
//        }
        return dividedOrderItem;
    }



    //사람수대로 나누기 -> 토탈금액을 나누면 된다.
    //각각 계산하는 메뉴 나누기 -> 오더를 하나 더 만들어서 오더 아이템을 옮겨담는식.

    //아래 두가지는 추후 구현 예정.
    //맥주는 5등분, 음식은 3등분 같은식으로 나누기 -> 위 두가지 방법을
    //3명은 20불씩, 나머지는 마지막 한사람이 다 내는식;



}
