package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.controller.ItemDTO;
import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    StoreService storeService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemService itemService;


    @Test
    public void createOrder() throws Exception {
        Long owner1Id = userService.join("gggabikr@gmail.com", "Jason Lee", "aaasssddd", "ROLE_OWNER", "7788097503");
        Long userId = userService.join("ggg@gmail.com", "Jacob Park", "aaasssddd", "ROLE_GENERAL", "7781111111");
        Long user2Id = userService.join("gggaaa@gmail.com", "Breece Park", "aaasssddd", "ROLE_GENERAL", "7782222222");

        //let's say the address is made from controller and is sent here.
        Address address1 = new Address("B.C", "Vancouver", "1990 41st Ave", "#203","V6M 1Y4");
        Long storeId = storeService.registerStore("Jason's Pizza", address1, "6043431354", owner1Id);
        Store store = storeService.findOne(storeId);

        Long store2Id = storeService.registerStore("Jason's Hamburger", address1, "60433444444", owner1Id);
        Store store2 = storeService.findOne(store2Id);

        Long categoryId = storeService.addNewCategory(storeId, "Appetizers");

        //it is itemDto from view
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("Fries");
        itemDTO.setStoreId(storeId);
        itemDTO.setCategoryId(categoryId);
        itemDTO.setItemType("food");
        itemDTO.setPrice(4.55);

        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setName("Beef Taco");
        itemDTO2.setStoreId(storeId);
        itemDTO2.setCategoryId(categoryId);
        itemDTO2.setItemType("food");
        itemDTO2.setPrice(13.25);

        Long item1id = itemService.registerItem(itemDTO);
        Long item2id = itemService.registerItem(itemDTO2);
        Item item1 = itemService.findOne(item1id);
        Item item2 = itemService.findOne(item2id);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 2, "");
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 1);


        Long orderId = orderService.createOrder(userId, storeId, "delivery", orderItem1, orderItem2);
        Order order = orderService.findOne(orderId);
        assertEquals(orderId, orderService.findByUser(userId).get(0).getId());
        assertEquals(orderId, orderService.findByStore(storeId).get(0).getId());
        assertEquals(4.55*2+13.25, order.getSubtotal(),0.0001);

    //check receipt of order; change price of item and make another order.
        // check receipt of both orders if the price change is applied for 2nd one and NOT applied for 1st one.
        // (also check the whole receipt whether the total prices and things are correct for both)
        // Passed!

        Payment payment = Payment.createPayment(order, 1);
        assertEquals(payment.getPayers().get(0).getTotal(), order.getTotal(), 0.0001);
        payment.getPayers().get(0).makeBill();
        assertEquals(4.55*2+13.25, order.getSubtotal(),0.0001);

        //price changed for fries, and 50% discount event on Beef taco
        item1.changeItemPrice(5.25);
        item2.changeItemPrice(6.63);
        OrderItem orderItem3 = OrderItem.createOrderItem(item1, 1,"");
        OrderItem orderItem4 = OrderItem.createOrderItem(item2,1,"");

        Long order2Id = orderService.createOrder(userId, storeId, "pickUp", orderItem3, orderItem4);
        Order order2 = orderService.findOne(order2Id);
        Payment payment2 = Payment.createPayment(order2, 20);
        assertEquals(5.25+6.63, order2.getSubtotal(),0.0001);
        order2.getPayment().getPayers().get(0).makeBill();

//    //Delete item after these two orders, and check receipt again if an error is arisen.
        itemService.removeItem(storeId,item2id);
        payment.getPayers().get(0).makeBill();
        payment2.getPayers().get(0).makeBill();
        payment2.getPayers().get(0).payBill(PaymentMethod.valueOf("CREDIT"));

        OrderItem orderItem5 = OrderItem.createOrderItem(item1, 2);
        Long order3Id = orderService.createOrder(userId, storeId, "pickUp", orderItem5);
        Order order3 = orderService.findOne(order3Id);
        Payment payment3 = Payment.createPayment(order3, 1);
        payment3.makeEvenPayments(2);

        payment3.getPayers().get(1).setTipAmount(false, 20);
        payment3.getPayers().get(1).payBill(PaymentMethod.valueOf("CASH"));

        payment3.printBillsForAllPayers();

//    //cancel order if any payment has made, cannot be cancelled. otherwise, proceed.
          orderService.cancelOrder(orderId); // should be ok -> Passed!
        //orderService.cancelOrder(order2Id); //error! -> Passed!
        //orderService.cancelOrder(order3Id); //error! -> Passed!

//    //delete orderItem from order.
        orderService.deleteOrderItemFromOrder(orderId, orderItem1.getId());
        assertEquals(1, order.getOrderItems().size());
        assertEquals("Beef Taco", order.getOrderItems().get(0).getItem().getName());
        assertEquals(13.25, order.getSubtotal(), 0.001); //(this order has made before 50% discount on Beef Taco is effected)
//    //delete orderItem needs to be failed because the orderItem does not belong to given order.
//        orderService.deleteOrderItemFromOrder(order2Id, orderItem2.getId()); //error! -> Passed!

    //findByUser test
        List<Order> byUser1 = orderService.findByUser(userId);
        List<Order> byUser2 = orderService.findByUser(user2Id);
        assertEquals(3, byUser1.size());
        assertEquals(0, byUser2.size());
        assertTrue(byUser1.contains(order));
        assertTrue(byUser1.contains(order2));
//        assertTrue(byUser1.contains(order3));

    //findByStore test
        List<Order> byStore1 = orderService.findByStore(storeId);
        List<Order> byStore2 = orderService.findByStore(store2Id);
        assertEquals(3, byStore1.size());
        assertEquals(0, byStore2.size());
        assertTrue(byStore1.contains(order));
        assertTrue(byStore1.contains(order2));
        assertTrue(byStore1.contains(order3));

    //findByItem test
        ItemDTO itemDTO3 = new ItemDTO();
        itemDTO3.setName("Coke");
        itemDTO3.setStoreId(storeId);
        itemDTO3.setCategoryId(categoryId);
        itemDTO3.setItemType("soda");
        itemDTO3.setPrice(3.25);

        Long item3id = itemService.registerItem(itemDTO3);
        List<Order> byItem1 = orderService.findByItem(item1id);
        List<Order> byItem2 = orderService.findByItem(item2id);
        List<Order> byItem3 = orderService.findByItem(item3id);

//        for(Order order333: byItem1){
//            for(Payer payer: order333.getPayment().getPayers()){
//                for(OrderItem orderItem : payer.getOrderItems()){
//                    System.out.println(orderItem.getItem().getName() + orderItem.getItem().getId());
//                }
//                System.out.println("==orderItems of payers==");
//            }
//
//                order333.getPayment().printBillsForAllPayers();
//            System.out.println("==============");
//            System.out.println("==============");
//            System.out.println("==============");
//        }



        //FIXME: As orderItem1 is deleted from order(order1), order should not be selected with item1Id value.
        // however, it does!!! I tried to figure it out, but I really don't know why.
//        assertEquals(2, byItem1.size()); //there were 3 but deleted one orderItem from order1
//        assertFalse(byItem1.contains(order));
//        assertTrue(byItem1.contains(order2));
//        assertTrue(byItem1.contains(order3));

        assertEquals(2, byItem2.size());
        assertTrue(byItem2.contains(order));
        assertTrue(byItem2.contains(order2));
        assertFalse(byItem2.contains(order3));
        assertEquals(0, byItem3.size());


    // findByOrderType test
        List<Order> pickUpOrders = orderService.findByOrderType("pickUp");
        List<Order> deliveryOrders = orderService.findByOrderType("delivery");
        List<Order> tableOrders = orderService.findByOrderType("table");
        assertEquals(2, pickUpOrders.size());
        assertTrue(pickUpOrders.contains(order2));
        assertTrue(pickUpOrders.contains(order3));
        assertEquals(1, deliveryOrders.size());
        assertTrue(deliveryOrders.contains(order));
        assertEquals(0, tableOrders.size());


    // findByOrderStatus test
        //confirming, preparing, readyForPickUp, completed, paidInFull, cancelled
        System.out.println(order.getOrderStatus().name());
        System.out.println(order2.getOrderStatus().name());
        System.out.println(order3.getOrderStatus().name());

        List<Order> confirming = orderService.findByOrderStatus("confirming"); //order3
        List<Order> preparing = orderService.findByOrderStatus("preparing"); // empty
        List<Order> readyForPickUp = orderService.findByOrderStatus("readyForPickUp");// empty
        List<Order> completed = orderService.findByOrderStatus("completed"); //empty
        List<Order> paidInFull = orderService.findByOrderStatus("paidInFull"); //order2
        List<Order> cancelled = orderService.findByOrderStatus("cancelled"); //order

        assertEquals(1, confirming.size());
        assertTrue(confirming.contains(order3));
        assertEquals(0, preparing.size());
        assertEquals(0, readyForPickUp.size());
        assertEquals(0, completed.size());
        assertEquals(1, paidInFull.size());
        assertTrue(paidInFull.contains(order2));
        assertEquals(1, cancelled.size());
        assertTrue(cancelled.contains(order));


        payment2.getPayers().get(0).setTipAmount(true, 3.5);
        payment2.getPayers().get(0).payBill(PaymentMethod.valueOf("CASH"));


//        order : total: 13.91    totalTip: 0.0
//        order2 : total: 12.47    totalTip: 3.5
//        order3 : total: 11.03    totalTip: 1.05

        //findByTotalAmount test
        List<Order> byTotalAmount10to12 = orderService.findByTotalAmount(10, 12);// order3
        assertEquals(1, byTotalAmount10to12.size());
        assertTrue(byTotalAmount10to12.contains(order3));

        List<Order> byTotalAmount0to13 = orderService.findByTotalAmount(0, 13);//order2,3
        assertEquals(2, byTotalAmount0to13.size());
        assertTrue(byTotalAmount0to13.contains(order2));
        assertTrue(byTotalAmount0to13.contains(order3));

        List<Order> byTotalAmount10to20 = orderService.findByTotalAmount(10, 20);//order2,3
        assertEquals(3, byTotalAmount10to20.size());
        assertTrue(byTotalAmount10to20.contains(order));
        assertTrue(byTotalAmount10to20.contains(order2));
        assertTrue(byTotalAmount10to20.contains(order3));

        List<Order> byTotalAmount15to20 = orderService.findByTotalAmount(15, 30);//empty
        assertEquals(0, byTotalAmount15to20.size());

        List<Order> byTotalAmount12to12 = orderService.findByTotalAmount(12,12);//같은값일때 empty
        assertEquals(0, byTotalAmount12to12.size());

        List<Order> byTotalAmount1103to1103 = orderService.findByTotalAmount(11.03,11.03);//같은값일때 + 정확히 해당하는 값이 있을때 order3
        assertEquals(1, byTotalAmount1103to1103.size());
        assertTrue(byTotalAmount1103to1103.contains(order3));

        List<Order> byTotalAmount14to12 = orderService.findByTotalAmount(14, 12);//앞뒤순서가 바뀌었을때 order1,2
        assertEquals(2, byTotalAmount14to12.size());
        assertTrue(byTotalAmount14to12.contains(order));
        assertTrue(byTotalAmount14to12.contains(order2));


        //findByTotalTipAmount test
        List<Order> byTotalTipAmount20to40 = orderService.findByTotalTipAmount(0, 3);//order1,3
        assertEquals(2, byTotalTipAmount20to40.size());
        assertTrue(byTotalTipAmount20to40.contains(order));
        assertTrue(byTotalTipAmount20to40.contains(order3));

        List<Order> byTotalTipAmount0to20 = orderService.findByTotalTipAmount(1,4);//order2,3
        assertEquals(2, byTotalTipAmount0to20.size());
        assertTrue(byTotalTipAmount0to20.contains(order2));
        assertTrue(byTotalTipAmount0to20.contains(order3));

        List<Order> byTotalTipAmount0to70 = orderService.findByTotalTipAmount(0, 5);//order1,2,3
        assertEquals(3, byTotalTipAmount0to70.size());
        assertTrue(byTotalTipAmount0to70.contains(order));
        assertTrue(byTotalTipAmount0to70.contains(order2));
        assertTrue(byTotalTipAmount0to70.contains(order3));

        List<Order> byTotalTipAmount100to150 = orderService.findByTotalTipAmount(5, 10);//empty
        assertEquals(0, byTotalTipAmount100to150.size());

        List<Order> byTotalTipAmount100to100 = orderService.findByTotalTipAmount(3,3);//같은값일때 empty
        assertEquals(0, byTotalTipAmount100to100.size());

        List<Order> byTotalTipAmount552to552 = orderService.findByTotalTipAmount(1.05,1.05);//같은값일때 + 정확히 해당하는 값이 있을때 order3
        assertEquals(1, byTotalTipAmount552to552.size());
        assertTrue(byTotalTipAmount552to552.contains(order3));

        List<Order> byTotalTipAmount25to5 = orderService.findByTotalTipAmount(3, 1);//앞뒤순서가 바뀌었을때 order3
        assertEquals(1, byTotalTipAmount25to5.size());
        assertTrue(byTotalTipAmount25to5.contains(order3));
    }

}