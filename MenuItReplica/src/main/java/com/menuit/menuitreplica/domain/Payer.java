package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Payer {

    @Id @GeneratedValue
    @Column(name = "payer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String name;

    //임시.
    @ManyToMany @Transient
    private List<OrderItem> orderItems = new ArrayList<>();

    private double subtotal;

    private double additionalDiscount;

    private String discountReason;

    private double GST;

    private double PST;

    private double total;

    private double tipAmount;

    private PaymentMethod paymentMethod; //CASH, CREDIT, DEBIT, GIFTCARD

//    private double paidAmountInclTip;

    private boolean paid; // 0 = not yet paid, 1 = paid

    @Column(name = "receipt")
    @Lob
    private String receiptData;

    public double getSubtotal(){
        double subtotal = 0;
        for(OrderItem orderItem: getOrderItems()){
            double subtotalEach = orderItem.getOrderPrice() * orderItem.getCount();
            subtotal +=subtotalEach;
        }
        return Math.round(subtotal * 100) / 100.0;
    }

    public double getGST(){
        setGST(Math.round((getSubtotal()-getAdditionalDiscount()) * 5)/100.0);
        return Math.round((getSubtotal()-getAdditionalDiscount()) * 5)/100.0;
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
        double total = Math.round((getSubtotal() + getGST() + getPST()-getAdditionalDiscount()) * 100) / 100.0;
        setTotal(total);
        return total;
    }

    public void moveItemToAnotherPayer(OrderItem orderItem, Payer payer, int count){
        if(getOrderItems().contains(orderItem)){
            OrderItem newOrderItem = orderItem.clone();
            newOrderItem.setCount(count);
            if(orderItem.getCount()>count){
                orderItem.setCount(orderItem.getCount()-count);
            } else {
                getOrderItems().remove(orderItem);
            }
            boolean foundSameItem = false;
            for(OrderItem orderItem1:payer.getOrderItems()){
                if(orderItem1.getItem().equals(newOrderItem.getItem())){
                    orderItem1.setCount(orderItem1.getCount()+count);
                    foundSameItem = true;
                    break;
                }
            }
            if(!foundSameItem){
                payer.getOrderItems().add(newOrderItem);
            }
        } else {
            throw new NullPointerException("Selected orderItem is not exist for the payer.");
        }
    }


    public String makeStringBlock(int length, Long lon){
        return makeStringBlock(length, Long.toString(lon));
    }

    public String makeStringBlock(int length, double db){
        return makeStringBlock(length, Double.toString(db));
    }

    public String makeStringBlock(int length, int integer){
        return makeStringBlock(length, Integer.toString(integer));
    }

    public String makeStringBlock(int length, String str){
        String result = "";
        int strLength = str.length();
        if(strLength<length){
            result += (str + " ".repeat(length-strLength));
        } else{
            result = str.substring(0,length-1) +" ";
        }
        return result;
    }

    public String makeStringBlockAtFront(int length, Long lon){
        return makeStringBlockAtFront(length, Long.toString(lon));
    }

    public String makeStringBlockAtFront(int length, double db){
        return makeStringBlockAtFront(length, Double.toString(db));
    }

    public String makeStringBlockAtFront(int length, int integer){
        return makeStringBlockAtFront(length, Integer.toString(integer));
    }


    public String makeStringBlockAtFront(int length, String str){
        //block at the front.
        String result = "";
        int strLength = str.length();
        if(strLength<length){
            result += (" ".repeat(length-strLength) + str);
        } else{
            result = str.substring(0,length-1);
        }
        return result;
    }

    public String makeBill(){
        StringBuilder sb = new StringBuilder();
        sb.append("============== Receipt ================\n");
        Store store = this.getPayment().getOrder().getStore();
        sb.append("Store: ").append(makeStringBlock(19, store.getName()))
                .append("Order: ").append(makeStringBlockAtFront(6, this.getPayment().getOrder().getId())).append("\n");

        String type = "";
        OrderType orderType = this.getPayment().getOrder().getOrderType();
        if(orderType.equals(OrderType.table)){
            type = "Dine-In";
        } else if(orderType.equals(OrderType.pickUp)){
            type = "Pick-Up";
        } else if(orderType.equals(OrderType.delivery)){
            type = "Delivery";
        } else {
            type = "null";
        }
        sb.append("Phone: ").append(store.getPhone())
                .append("    Type: ").append(makeStringBlockAtFront(12, type)).append("\n");

        if(orderType == OrderType.table){
            sb.append("Table #: ").append(this.getPayment().getOrder().getUser().getFullName()).append("\n");
        } else{
            sb.append(makeStringBlock(20, "Customer Name:")).append(makeStringBlock(19, this.getPayment().getOrder().getUser().getFullName())).append("\n");
            sb.append("Customer Contact:   ").append(this.getPayment().getOrder().getUser().getPhone()).append("\n");
        }
        sb.append("Address: ").append(store.getAddressString()).append("\n");
        sb.append("=======================================\n");
        sb.append("Item               Qnt   Unit     Price\n");
        for(OrderItem orderItem: this.getOrderItems()){
            sb.append(makeStringBlock(20, orderItem.getItem().getName()))
                    .append(makeStringBlock(5, orderItem.getCount()))
                    .append(makeStringBlock(7, orderItem.getOrderPrice()))
                    .append(makeStringBlockAtFront(7, ("$" + orderItem.getTotalPrice()))).append("\n");
        }
        sb.append("=======================================\n");
        sb.append("Subtotal:").append(makeStringBlockAtFront(30, ("$" + getSubtotal()))).append("\n");

        if(this.additionalDiscount != 0 && this.discountReason.length()>5){
            sb.append(makeStringBlock(30, discountReason)).append(makeStringBlockAtFront(9, ("-$" + additionalDiscount))).append("\n");
        }
        sb.append("GST:").append(makeStringBlockAtFront(35, ("$" + getGST()))).append("\n");
        sb.append("PST:").append(makeStringBlockAtFront(35, ("$" + getPST()))).append("\n");
        sb.append("Total:").append(makeStringBlockAtFront(33, ("$" + getTotal()))).append("\n");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);

        if(this.paid){
            sb.append("Tip:").append(makeStringBlockAtFront(35, ("$" + getTipAmount()))).append("\n");
            sb.append("========= Payment Information =========\n");
            if(this.paymentMethod != PaymentMethod.CASH){
                sb.append(this.paymentMethod.name()).append(": 0000-****-0000-****\n"); // to be replaced with actual card number
            } else{
                sb.append(this.paymentMethod.name()).append("\n");
            }
            sb.append("Paid amount: ").append(Math.round((this.total + this.tipAmount) * 100) / 100.0).append("\n");
        } else{
            if(!this.getPayment().isGratuity()){
                sb.append("=========== Tip suggestions ===========\n");
                sb.append("13%: ").append(makeStringBlockAtFront(34, ("$" + Math.round((getSubtotal() - getAdditionalDiscount()) * 13) / 100.0))).append("\n");
                sb.append("15%: ").append(makeStringBlockAtFront(34, ("$" + Math.round((getSubtotal() - getAdditionalDiscount()) * 15) / 100.0))).append("\n");
                sb.append("18%: ").append(makeStringBlockAtFront(34, ("$" + Math.round((getSubtotal() - getAdditionalDiscount()) * 18) / 100.0))).append("\n");
                sb.append("20%: ").append(makeStringBlockAtFront(34, ("$" + Math.round((getSubtotal() - getAdditionalDiscount()) * 20) / 100.0))).append("\n");
                sb.append("==========").append(formatDateTime).append("==========\n");
            } else{
                sb.append("========== Gratuity applied ===========\n");
                int gratuityPercent = getPayment().getGratuityPercent();
                double gratuityAmount = Math.round(getSubtotal() * gratuityPercent) / 100.0;
                sb.append(makeStringBlock(19, gratuityPercent + "%:"))
                        .append(makeStringBlockAtFront(20, "$" + gratuityAmount)).append("\n");
                sb.append("==========").append(formatDateTime).append("==========\n");
                sb.append("Amount due:").append(makeStringBlockAtFront(28, ("$" + Math.round((getTotal() + gratuityAmount) * 100) / 100.0))).append("\n");
            }
        }

        sb.append("============= Thank you! ==============\n\n");

        String toString = sb.toString();

        if(this.paid){
            this.receiptData = toString;
        }

        //printing result string to see if it looks okay
        System.out.println(toString);
        return toString;

    }



//    public void makeBill(){
//        System.out.println("===============Receipt=================");
//        Store store = this.getPayment().getOrder().getStore();
//        System.out.println("Store: "+ makeStringBlock(19, store.getName())+ "Order: " + makeStringBlockAtFront(6,this.getPayment().getOrder().getId()));
//
//        String type = "";
//        OrderType orderType = this.getPayment().getOrder().getOrderType();
//        if(orderType.equals(OrderType.table)){
//            type = "Dine-In";
//        } else if(orderType.equals(OrderType.pickUp)){
//            type = "Pick-Up";
//        } else if(orderType.equals(OrderType.delivery)){
//            type = "Delivery";
//        } else {
//            type = "null";
//        }
//
//        System.out.println("Phone: " + store.getPhone() + "    Type: " + makeStringBlockAtFront(12, type));
//        if(orderType == OrderType.table){
//            System.out.println("Table #: " + this.getPayment().getOrder().getUser().getFullName());
//        } else{
//            System.out.println(makeStringBlock(20,"Customer Name:") + makeStringBlock(19,this.getPayment().getOrder().getUser().getFullName()));
//            System.out.println("Customer Contact:   " + this.getPayment().getOrder().getUser().getPhone());
//        }
//        System.out.println("Address: " + store.getAddressString());
//        System.out.println("=======================================");
//        System.out.println("Item               Qnt   Unit     Price");
//        for(OrderItem orderItem: this.getOrderItems()){
//            System.out.println(makeStringBlock(20,orderItem.getItem().getName()) + makeStringBlock(5, orderItem.getCount()) + makeStringBlock(7, orderItem.getOrderPrice()) + makeStringBlockAtFront(7, ("$"+ orderItem.getTotalPrice())));
//        }
//        System.out.println("=======================================");
//        System.out.println("Subtotal:"+ makeStringBlockAtFront(30,("$"+ getSubtotal())));
//
//        if(this.additionalDiscount != 0 && this.discountReason.length()>5){
//            System.out.println(makeStringBlock(30,discountReason) + makeStringBlockAtFront(9, ("-$" + additionalDiscount)));
//        }
//
//        System.out.println("GST:"+makeStringBlockAtFront(35, ("$" + getGST())));
//        System.out.println("PST:"+makeStringBlockAtFront(35, ("$" + getPST())));
//        System.out.println("Total:"+makeStringBlockAtFront(33, ("$" + getTotal())));
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formatDateTime = now.format(formatter);
//
//        if(this.paid){
//            System.out.println("Tip:" + makeStringBlockAtFront(35, ("$" + getTipAmount())));
//            System.out.println("==========Payment Information==========");
//            if(this.paymentMethod != PaymentMethod.CASH){
//                System.out.println(this.paymentMethod.name() + ": 0000-****-0000-****"); // to be replaced with actual card number
//            } else{
//                System.out.println(this.paymentMethod.name());
//            }
//            System.out.println("Paid amount: " + Math.round((this.total+ this.tipAmount)*100)/100.0);
//        } else{
//            if(!this.getPayment().isGratuity()){
//                System.out.println("============Tip suggestions============");
//                System.out.println("13%: " + makeStringBlockAtFront(34,("$"+Math.round((getSubtotal()-getAdditionalDiscount())*13)/100.0)));
//                System.out.println("15%: " + makeStringBlockAtFront(34,("$"+Math.round((getSubtotal()-getAdditionalDiscount())*15)/100.0)));
//                System.out.println("18%: " + makeStringBlockAtFront(34,("$"+Math.round((getSubtotal()-getAdditionalDiscount())*18)/100.0)));
//                System.out.println("20%: " + makeStringBlockAtFront(34,("$"+Math.round((getSubtotal()-getAdditionalDiscount())*20)/100.0)));
//                System.out.println("==========" + formatDateTime + "==========");
//            } else{
//                System.out.println("===========Gratuity applied============");
//                int gratuityPercent = getPayment().getGratuityPercent();
//                double gratuityAmount = Math.round(getSubtotal() * gratuityPercent) / 100.0;
//                System.out.println(makeStringBlock(19, gratuityPercent+"%:")+ makeStringBlockAtFront(20, "$"+gratuityAmount));
//                System.out.println("==========" + formatDateTime + "==========");
//                System.out.println("Amount due:"+ makeStringBlockAtFront(28, ("$"+Math.round((getTotal() + gratuityAmount)*100)/100.0)));
//            }
//        }
//        System.out.println("==============Thank you!===============");
//        System.out.println();
//        System.out.println();
//    }

    public double tipPercentCalculator(double percentage){
        return Math.round(getSubtotal()*percentage)/100.0;
    }

    //TODO: Gratuity applied 일때 자동적으로 팁 지정되도록!!!
    public void setTipAmount(boolean boo, double number){
        if(!boo){  //if boo = false, number is percentage of tip
            setTipAmount(tipPercentCalculator(number));
        } else{  //if boo = true, number is amount of tip
            setTipAmount(number);
        }
        this.payment.setTotalTipAmount();
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
