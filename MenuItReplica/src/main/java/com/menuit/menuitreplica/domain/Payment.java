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

    private double total;

    private double totalTipAmount;

    private boolean isGratuity;

    private boolean status; //true = fully paid, false = not yet paid fully

    //payer 이고 뭐고 다 때려치고 스트링으로 영수증 만들어내는거나 만들까 생각중.
    // 그경우엔 여기에 total, paidAmount, tipAmount 만들고 페이 될때마다 paidAmount 늘려서
    // total 과 paidAmount 의 차이가 5센트 이하가 되면 orderStatus 를 paidInFull 로 상태를 변경하는 방법을 사용할 예정.

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

    public boolean checkPayments(){
        double paidAmount = 0;
        for (Payer payer: getPayers()){
            if(payer.isPaid()) {
                paidAmount += payer.getTotal();
            }
        }

        //difference between total and paid total.
        double difference = getTotal() - paidAmount;
        if (difference <=0 || Math.abs(difference)<getTotal()*0.01){
            setStatus(true);
            setTotalTipAmount();
            return true;
        } else{
            return false;
        }
    }


    public void setTotalTipAmount(){
        double totalTipAmount = 0;
        for (Payer payer: getPayers()){
            if(payer.isPaid()) {
                totalTipAmount += payer.getTipAmount();
            }
        }
        setTotalTipAmount(totalTipAmount);
    }

    //==결제 방법에 따라 다른 결제 방식, 단계 구현하기==//

        //사람수대로 나누기 -> 토탈금액을 나누면 된다. -->구현완료//테스트 미완

        //각각 계산하는 메뉴 나누기 -> 오더를 하나 더 만들어서 오더 아이템을 옮겨담는식. -->구현예정

        //3명은 20불씩, 나머지는 마지막 한사람이 다 내는식; --> 구현예정.
        // payer에 orderItem, GST, PST 계산 없이 total과 tipAmount만 세팅하고,
        // Payment에서는 그냥 total 에서 payer.total의 금액만큼 제외하기 && totalTipAmount에 tipAmount더하기.

        //맥주는 5등분, 음식은 3등분 같은식으로 나누기 -> 구현 안함


}
