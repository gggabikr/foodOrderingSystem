package com.menuit.menuitreplica.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id @GeneratedValue
    @Column(name="payment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private double total;

    private double additionalDiscountAmount; //special event, coupon, employee discount..etc

    private double totalTipAmount;

    private boolean isGratuity;

    private int gratuityPercent;

    private boolean status; //true = fully paid, false = not yet paid fully

    private String comment;

    //payer 이고 뭐고 다 때려치고 스트링으로 영수증 만들어내는거나 만들까 생각중.
    // 그경우엔 여기에 total, paidAmount, tipAmount 만들고 페이 될때마다 paidAmount 늘려서
    // total 과 paidAmount 의 차이가 5센트 이하가 되면 orderStatus 를 paidInFull 로 상태를 변경하는 방법을 사용할 예정.

    @OneToMany(cascade = CascadeType.ALL)
    private List<Payer> payers = new ArrayList<>();

    public static Payment createPayment(Order order, int numOfCustomers){
        return createPayment(order,numOfCustomers,0,"");
    }


    public static Payment createPayment(Order order, int numOfCustomers, double additionalDiscountAmount, String discountReason){
        Payment payment = new Payment();
        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItem orderItem:order.getOrderItems()){
            orderItems.add(orderItem.clone());
        }
        payment.setTotal(order.getTotal());
        payment.setGratuity(order.getStore().getGratuity() <= numOfCustomers);
        payment.setGratuityPercent(order.getStore().getGratuityPercent());
        payment.setStatus(false);
        payment.setAdditionalDiscountAmount(additionalDiscountAmount);
        payment.setComment(discountReason);
        payment.setTotalTipAmount(0);

        Payer payer = new Payer();
        payer.setPayment(payment);
        payer.setOrderItems(orderItems);
        payer.setSubtotal(order.getSubtotal());
        payer.setPST(order.getPST());
        payer.setGST(order.getGST());
        payer.setTotal(order.getTotal());
        payer.setPaid(false);
        payer.setAdditionalDiscount(additionalDiscountAmount);
        payer.setDiscountReason(discountReason);

        payment.getPayers().add(payer);

        order.setPayment(payment);

        return payment;
    }

    public void makeEvenPayments(int numOfCustomers){
        for(int i=1; i<numOfCustomers; i++){
            Payer payer = new Payer();
            payer.setName("Customer " + i);
            payer.setPayment(this);
            payer.setPaid(false);
            payers.add(payer);
        }

        List<OrderItem> dividedOrderItems = new ArrayList<>();
        for(OrderItem orderItem: getOrder().getOrderItems()){
            dividedOrderItems.add(divideOrderItem(orderItem, numOfCustomers));
        }
        for(Payer payer: payers){
            payer.setOrderItems(dividedOrderItems);
            payer.setAdditionalDiscount(Math.round((getAdditionalDiscountAmount()/numOfCustomers*100)/100.0));
            payer.setDiscountReason(comment);
        }
    }

    public Payer addPayer(){
        Payer payer = new Payer();
        payer.setName("Customer");
        payer.setPayment(this);
        payer.setPaid(false);
        payers.add(payer);
        return payer;
    }


    public OrderItem divideOrderItem(OrderItem orderItem, int num){
        OrderItem dividedOrderItem = new OrderItem();
        dividedOrderItem.setItem(orderItem.getItem());
        dividedOrderItem.setOrderPrice(Math.round((orderItem.getOrderPrice()/num)*100)/100.0);
        dividedOrderItem.setCount(orderItem.getCount());
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
        double difference = getTotal()-getAdditionalDiscountAmount() - paidAmount;
        //if paidAmount is bigger than the total or the difference is less than 20 cents, it will accept it as paid all.
        if (difference <=0 || Math.abs(difference)<0.2){
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
        setTotalTipAmount(Math.round(totalTipAmount*100)/100.0);
    }

    public void deletePayer(Payer payer) throws IllegalAccessException {
        if(!payer.isPaid() && payer.getOrderItems().size() ==0){
            this.getPayers().remove(payer);
        } else {
            throw new IllegalAccessException("Payer cannot be deleted unless its orderItem list is empty and payer did not make a payment.");
        }
    }

    public void toggleGratuity(){
        this.isGratuity = !this.isGratuity;
    }

    public void setAdditionalDiscountAmount(double amount, String comment){
        //need to set minimum length of comment to be 5 at the view page
        this.additionalDiscountAmount = amount;
        this.comment = comment;
    }

    public void setAdditionalDiscountPercent(int discountPercent, String comment){
        this.additionalDiscountAmount = Math.round(this.getOrder().getSubtotal()*(100-discountPercent))/100.0;
        this.comment = comment;
    }


    //==결제 방법에 따라 다른 결제 방식, 단계 구현하기==//

        //사람수대로 나누기 -> 토탈금액을 나누면 된다. -->구현완료//테스트 미완

        //각각 계산하는 메뉴 나누기 -> 오더를 하나 더 만들어서 오더 아이템을 옮겨담는식. -->구현예정

        //3명은 20불씩, 나머지는 마지막 한사람이 다 내는식; --> 구현예정.
        // payer에 orderItem, GST, PST 계산 없이 total과 tipAmount만 세팅하고,
        // Payment에서는 그냥 total 에서 payer.total의 금액만큼 제외하기 && totalTipAmount에 tipAmount더하기.

        //맥주는 5등분, 음식은 3등분 같은식으로 나누기 -> 구현 안함


}
