package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.controller.ItemDTO;
import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.ItemRepository;
import com.menuit.menuitreplica.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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


        Long orderId = orderService.createOrder(userId, storeId, "pickUp", orderItem1, orderItem2);
        Order order = orderService.findOne(orderId);
        assertEquals(orderId, orderService.findByUser(userId).get(0).getId());
        assertEquals(orderId, orderService.findByStore(storeId).get(0).getId());
        assertEquals(4.55*2+13.25, order.getSubtotal(),0.0001);

        //TODO: check receipt of order; change price of item and make another order.
        // check receipt of both orders if the price change is applied for 2nd one and NOT applied for 1st one.
        // (also check the whole receipt whether the total prices and things are correct for both)
        // Passed!

        //TODO: Delete item after these two orders, and check receipt again if an error is arise.
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

        //TODO: cancel order if any payment has made, cannot be cancelled. otherwise, proceed.
//        orderService.cancelOrder(orderId); // should be ok -> Passed!
//        orderService.cancelOrder(order2Id); //error! -> Passed!
//        orderService.cancelOrder(order3Id); //error! -> Passed!

        //
    }


    @Test
    public void deleteOrderItemFromOrder() {
    }

    @Test
    public void findOne() {
    }

    @Test
    public void findOneOrderItem() {
    }

    @Test
    public void findByUser() {
    }

    @Test
    public void findByStore() {
    }

    @Test
    public void findByItem() {
    }

    @Test
    public void findByOrderType() {
    }

    @Test
    public void findByOrderStatus() {
    }

    @Test
    public void findOrdersBetweenTwoTimes() {
    }

    @Test
    public void findByTotalAmount() {
    }

    @Test
    public void findByTotalTipAmount() {
    }

    @Test
    public void findByUserAndStore() {
    }
}