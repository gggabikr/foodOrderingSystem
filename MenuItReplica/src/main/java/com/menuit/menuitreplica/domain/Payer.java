package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Payer {

    @Id @GeneratedValue
    @Column(name = "payer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String name;

    //임시.
    @ManyToMany
    private List<OrderItem> orderItems = new ArrayList<>();

    private double subtotal;

    private double GST;

    private double PST;

    private double total;

    private double tipAmount;

    private PaymentMethod paymentMethod; //CASH, CREDIT, DEBIT, GIFTCARD

//    private double paidAmountInclTip;

    private boolean paid; // 0 = not yet paid, 1 = paid

    public double getSubtotal(){
        double subtotal = 0;
        for(OrderItem orderItem: getOrderItems()){
            double subtotalEach = orderItem.getOrderPrice() * orderItem.getCount();
            subtotal +=subtotalEach;
        }
        return Math.round(subtotal * 100) / 100.0;
    }

    public double getGST(){
        setGST(Math.round(getSubtotal() * 5)/100.0);
        return Math.round(getSubtotal() * 5)/100.0;
    }

    public double getPST(){
        double alcoholic = 0;
        double soda = 0;
        for(OrderItem orderItem: getOrderItems()){
            double subtotalEach = orderItem.getOrderPrice() * orderItem.getCount();
            ItemType itemType = orderItem.getItem().getItemType();

            if(itemType.equals(ItemType.alcoholic)){
                alcoholic += subtotalEach;
            } else if(itemType.equals(ItemType.soda)){
                soda += subtotalEach;
            }
        }
        double PstBeforeRounding = alcoholic * 0.1 + soda * 0.07;
        double PstAfterRounding = Math.round(PstBeforeRounding * 100) / 100.0;
        setPST(PstAfterRounding);
        return PstAfterRounding;
    }

    public double getTotal(){
        double total = Math.round((getSubtotal() + getGST() + getPST()) * 100) / 100.0;
        setTotal(total);
        return total;
    }

    public void makeBill(){
        System.out.println("===============Receipt==============");
        Store store = this.getPayment().getOrder().getStore();
        System.out.println("Store: "+ store.getName()+"    "+ "Order: " + this.getPayment().getOrder().getId());
        System.out.println("Phone: " + store.getPhone());
        System.out.println("Address: " + store.getAddress());
        System.out.println("===============================");
        System.out.println("Item               Qnt    Price");
        for(OrderItem orderItem: this.getOrderItems()){
            System.out.println(orderItem.getItem().getName() + "   1    " + orderItem.getOrderPrice());
        }
        System.out.println("Subtotal: " + getSubtotal());
        System.out.println("GST: " + getGST());
        System.out.println("PST: " + getPST());
        System.out.println("Total: " + getTotal());
        System.out.println("===============================");
        if(!this.getPayment().isGratuity()){
            System.out.println("========Tip suggestions========");
            System.out.println("13%: " + Math.round(getSubtotal()*100)/100);
            System.out.println("15%: " + Math.round(getSubtotal()*100)/100);
            System.out.println("18%: " + Math.round(getSubtotal()*100)/100);
            System.out.println("20%: " + Math.round(getSubtotal()*100)/100);
            System.out.println("=====" + LocalDateTime.now() + "=====");
            System.out.println("==========Thank you!===========");
        } else{
            System.out.println("");
        }
    }

    public double tipPercentCalculator(double percentage){
        return Math.round(getSubtotal()*percentage)/100.0;
    }

    public void setTipAmount(boolean boo, double number){
        if(!boo){  //if boo = 0, number is percentage of tip
            setTipAmount(tipPercentCalculator(number));
        } else{  //if boo = 1, number is amount of tip
            setTipAmount(number);
        }
    }

    public double payBill(PaymentMethod paymentMethod){
        setPaymentMethod(paymentMethod);
        double amount = getTotal() + this.tipAmount;
        setPaid(true);
        if(this.getPayment().checkPayments()){
            this.getPayment().getOrder().setOrderStatus(OrderStatus.paidInFull);
        }
        return amount;
    }
}
