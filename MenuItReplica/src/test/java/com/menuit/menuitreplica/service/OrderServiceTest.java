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
        OrderItem orderItem3 = OrderItem.createOrderItem(item1, 2,"");
        OrderItem orderItem4 = OrderItem.createOrderItem(item2, 4);


        Long orderId = orderService.createOrder(userId, storeId, "pickUp", orderItem1, orderItem2);
        Order order = orderService.findOne(orderId);
        assertEquals(orderId, orderService.findByUser(userId).get(0).getId());
        assertEquals(orderId, orderService.findByStore(storeId).get(0).getId());
        assertEquals(4.55*2+13.25, order.getSubtotal(),0.0001);

        Payment payment = Payment.createPayment(order, 1);
        assertEquals(payment.getPayers().get(0).getTotal(), order.getTotal(), 0.0001);
        payment.getPayers().get(0).makeBill();
        item1.changeItemPrice(5.25);
        payment.getPayers().get(0).makeBill();
        assertEquals(4.55*2+13.25, order.getSubtotal(),0.0001);

//        Long order2Id = orderRepository.createOrder(Order.createTableOrder(userService.findOneById(userId),store,orderItem3));

        Long order2Id = orderService.createOrder(userId, storeId, "pickUp", orderItem3);
        Order order2 = orderService.findOne(order2Id);
        Payment.createPayment(order2,20);
        assertEquals(5.25*2, order2.getSubtotal(),0.0001);
        order2.getPayment().getPayers().get(0).makeBill();


        //TODO: 오더하고 기록 확인, 아이템 가격바꾼후 오더하고 둘 다의 기록 확인. 기록에 이상없는지(기존 오더의 토탈 가격등이 달라지지않았는지.)
        //TODO: 그후 아이템 삭제. 다시 기록확인. 그래도 정상적으로 기록확인이 되는지.
    }

    @Test
    public void cancelOrder() {
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