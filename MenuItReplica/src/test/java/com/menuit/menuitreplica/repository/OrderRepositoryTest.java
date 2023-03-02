package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;


    @Test
    public void createOrder() throws Exception{

        //given
        Order order = new Order();

        Store store = new Store();
        storeRepository.register(store);
        order.setStore(store);

        User user = new User();
        user.setEmail("gggaaa@gmail.com");
        userRepository.save(user);
        order.setUser(user);

        //when
        Long orderId = orderRepository.createOrder(order);
        Order foundOrder = orderRepository.findOne(orderId);

        //then
        Assertions.assertEquals(order, foundOrder);
        Assertions.assertEquals(user, foundOrder.getUser());
        Assertions.assertEquals(store, foundOrder.getStore());
        Assertions.assertEquals("gggaaa@gmail.com", foundOrder.getUser().getEmail());
    }



    @Test
    public void findByUser() throws Exception {
        //given
        User user1 = new User();
        user1.setEmail("gggabikr@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_OWNER);

        Address address = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");

        User user2 = new User();
        user2.setEmail("gggab@gmail.com");
        user2.setFullName("Jacob Lee");
        user2.setPassword("abcdef");
        user2.setPhone("7787503982");
        user2.setRole(UserRole.ROLE_OWNER);

        Address address2 = new Address("B.C", "Vancouver", "1999 61st Ave", "#305", "V6K 1Y1");


        Store store1 = new Store();
        store1.setOwner(user1);
        store1.setAddress(address);
        store1.setPhone("7788021324");
        store1.setStoreDescription("aaaaaa");
        store1.setName("Jason's donuts");
        store1.setStatus(true);

        Store store2 = new Store();
        store2.setOwner(user2);
        store2.setAddress(address2);
        store2.setPhone("6041234567");
        store2.setStoreDescription("aaaaaabb");
        store2.setName("Jacob's kitchen");
        store2.setStatus(false);

        Long cate1Id = storeRepository.addNewCategory(store1, "cate1");
        Long cate2Id = storeRepository.addNewCategory(store1, "cate2");
        Category cate1 = storeRepository.findOneCategory(cate1Id);
        Category cate2 = storeRepository.findOneCategory(cate2Id);


        Item item1 = new Item(store1, store1.getCategories().get(0),"Butter Chicken", 15.75, "food");
        Item item2 = new Item(store1, store1.getCategories().get(0),"Chicken Curry", 17.75, "food");
        Item item3 = new Item(store1, store1.getCategories().get(1),"Coke", 2.50, "soda");


        //when
        userRepository.save(user1);
        userRepository.save(user2);
        storeRepository.register(store1);
        storeRepository.register(store2);
        itemRepository.registerItem(store1, cate1, item1);
        itemRepository.registerItem(store1, cate1, item2);
        itemRepository.registerItem(store1, cate2, item3);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 2, "");
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 3, "");
        OrderItem orderItem3 = OrderItem.createOrderItem(item3, 1, "");
        Order order1 = new Order();

//        orderRepository.createOrder()

        //then
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