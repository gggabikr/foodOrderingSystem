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

        Store store = new Store();
        storeRepository.register(store);

        User user = new User();
        user.setEmail("gggaaa@gmail.com");
        userRepository.save(user);

        Order order = Order.createTableOrder(user, store);

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
    public void multiTest() throws Exception {
        //given
        User tableUser = new User();
        tableUser.setEmail("gggabikr1@gmail.com");
        tableUser.setFullName("Table 003");
        tableUser.setPassword("aabbccde");
        tableUser.setPhone("7788097503");
        tableUser.setRole(UserRole.ROLE_TABLE);


        User generalUser = new User();
        generalUser.setEmail("gggab2@gmail.com");
        generalUser.setFullName("Jacob Lee");
        generalUser.setPassword("abcdef");
        generalUser.setPhone("7787503982");
        Address address3 = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");
        generalUser.setAddress(address3);

        generalUser.setRole(UserRole.ROLE_GENERAL);


        User ownerUser1 = new User();
        ownerUser1.setEmail("gggabikr3@gmail.com");
        ownerUser1.setFullName("Table 003");
        ownerUser1.setPassword("aabbccde");
        ownerUser1.setPhone("7788097503");
        ownerUser1.setRole(UserRole.ROLE_OWNER);


        User ownerUser2 = new User();
        ownerUser2.setEmail("gggab4@gmail.com");
        ownerUser2.setFullName("Jacob Lee");
        ownerUser2.setPassword("abcdef");
        ownerUser2.setPhone("7787503982");
        ownerUser2.setRole(UserRole.ROLE_OWNER);



        userRepository.save(tableUser);
        userRepository.save(generalUser);
        userRepository.save(ownerUser1);
        userRepository.save(ownerUser2);


        Store store1 = new Store();
        store1.setOwner(ownerUser1);
        Address address = new Address("B.C", "Vancouver", "5321 donut Ave", "V6M 1Y4");
        store1.setAddress(address);
        store1.setPhone("7788021324");
        store1.setStoreDescription("aaaaaa");
        store1.setName("Jason's donuts");
        store1.setGratuity(5);
        store1.setGratuityPercent(15);
        store1.setStatus(true);

        Store store2 = new Store();
        store2.setOwner(ownerUser2);
        Address address2 = new Address("B.C", "Vancouver", "9977 Maple St", "#305", "V1A 3C4");
        store2.setAddress(address2);
        store2.setPhone("6041234567");
        store2.setStoreDescription("aaaaaabb");
        store2.setName("Jacob's kitchen");
        store2.setGratuity(6);
        store2.setGratuityPercent(15);
        store2.setStatus(false);

        storeRepository.register(store1);
        storeRepository.register(store2);

        Long cate1Id = storeRepository.addNewCategory(store1, "cate1");
        Long cate2Id = storeRepository.addNewCategory(store1, "cate2");
        Long cat1Id = storeRepository.addNewCategory(store2, "cat1");
        Category cate1 = storeRepository.findOneCategory(cate1Id);
        Category cate2 = storeRepository.findOneCategory(cate2Id);
        Category cat1 = storeRepository.findOneCategory(cat1Id);


        Item item1 = new Item(store1, store1.getCategories().get(0),"Butter Chicken", 15.75, "food");
        Item item2 = new Item(store1, store1.getCategories().get(0),"Chicken Curry", 17.75, "food");
        Item item3 = new Item(store1, store1.getCategories().get(1),"Coke", 2.50, "soda");

        Item itemA = new Item(store2, store2.getCategories().get(0), "Rice noodle soup", 17.25, "food");
        Item itemB = new Item(store2, store2.getCategories().get(0), "LemonGrass chicken", 15.50, "food");
        Item itemC = new Item(store2, store2.getCategories().get(0), "Spring rolls", 6.55, "food");
        Item itemD = new Item(store2, store2.getCategories().get(0), "Spicy rice noodle soup", 18.25, "food");
        Item itemE = new Item(store2, store2.getCategories().get(0), "Cold brew coffee", 5.25, "nonSoda");
        Item itemF = new Item(store2, store2.getCategories().get(0), "Kokanee Beer", 4.25, "alcoholic");






        //when
        itemRepository.registerItem(item1);
        itemRepository.registerItem(item2);
        itemRepository.registerItem(item3);
        itemRepository.registerItem(itemA);
        itemRepository.registerItem(itemB);
        itemRepository.registerItem(itemC);
        itemRepository.registerItem(itemD);
        itemRepository.registerItem(itemE);
        itemRepository.registerItem(itemF);



        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 2, "");
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 3, "");
        OrderItem orderItem3 = OrderItem.createOrderItem(item3, 1, "");
        OrderItem orderItem4 = OrderItem.createOrderItem(item2, 3, "");

        OrderItem orderItemA = OrderItem.createOrderItem(itemC, 1, "");
        OrderItem orderItemB = OrderItem.createOrderItem(itemE, 1, "");
        OrderItem orderItemC = OrderItem.createOrderItem(itemD, 3, "");
        OrderItem orderItemD = OrderItem.createOrderItem(itemA, 1, "");




        Order tableOrder = Order.createTableOrder(tableUser, store1, orderItem1, orderItem2);
        Order nonTableOrder = Order.createNonTableOrder(generalUser,store1,OrderType.delivery,orderItem4,orderItem3);

        Order tableOrder2 = Order.createTableOrder(tableUser, store2, orderItemA, orderItemB, orderItemC, orderItemD);
//        Order.createNonTableOrder(user2)

        Long tableOrderId = orderRepository.createOrder(tableOrder);
        orderRepository.createOrder(nonTableOrder);
        orderRepository.createOrder(tableOrder2);

        //then

        // 1. checking orders and their items, prices, etc
        Assertions.assertEquals(tableOrder, tableUser.getOrders().get(0));
        Assertions.assertEquals(nonTableOrder, generalUser.getOrders().get(0));
        Assertions.assertEquals(15.75*2+17.75*3, tableUser.getOrders().get(0).getSubtotal());
        Assertions.assertEquals(17.75*3+2.5, generalUser.getOrders().get(0).getSubtotal());
        Assertions.assertEquals(tableUser.getOrders().get(0), store1.getOrders().get(0));
        Assertions.assertEquals(generalUser.getOrders().get(0), store1.getOrders().get(1));

        Payment payment1 = Payment.createPayment(tableOrder, 3);
        Payment payment2 = Payment.createPayment(nonTableOrder, 20, 15, "employee discount");
        Payment payment3 = Payment.createPayment(tableOrder2, 5);

        // 2. test for receipt & its amounts, and dividing payments
        tableOrder.getPayment().getPayers().get(0).makeBill();
//        tableOrder2.getPayment().makeEvenPayments(5);
//        for(Payer payer: tableOrder2.getPayment().getPayers()){
//            payer.makeBill();
//            System.out.println("");
//        }

//        nonTableOrder.getPayment().getPayers().get(0).makeBill();
//        nonTableOrder.getPayment().makeEvenPayments(3);
//        nonTableOrder.getPayment().getPayers().get(0).makeBill();

        // 3. moving orderItem between payer and check if it worked right
//        testOrderPayer1.makeBill();
        tableOrder.getPayment().addPayer();
        Payer tableOrderPayer1 = tableOrder.getPayment().getPayers().get(0);
        Payer tableOrderPayer2 = tableOrder.getPayment().getPayers().get(1);
        tableOrderPayer1.moveItemToAnotherPayer(tableOrderPayer1.getOrderItems().get(0), payment1.getPayers().get(1), 2);
        tableOrderPayer1.moveItemToAnotherPayer(tableOrderPayer1.getOrderItems().get(0), payment1.getPayers().get(1), 1);
        tableOrder.getPayment().getPayers().get(0).makeBill();
        tableOrder.getPayment().getPayers().get(1).makeBill();
//
        // 4. check paying bill works fine and if it changes order status and record tip amount correctly
//        tableOrderPayer1.setTipAmount(true, 5.33);
//        tableOrderPayer1.payBill(PaymentMethod.valueOf("CREDIT"));
//        tableOrderPayer2.setTipAmount(true, 7.39);
//        tableOrderPayer2.payBill(PaymentMethod.valueOf("DEBIT"));
//
//        System.out.println(tableOrder.getOrderStatus()); //need to be paidInFull -> O
//        System.out.println(tableOrder.getPayment().getTotal()); //need to be 88.99 -> O
//        System.out.println(tableOrder.getPayment().getTotalTipAmount()); //need to be 12.71 -> O
//        System.out.println(tableOrder.getPayment().isStatus()); //need to be true -> O


        //5. deleting payer
//        tableOrder.getPayment().deletePayer(tableOrderPayer2); //error expected -> O
//        tableOrderPayer2.moveItemToAnotherPayer(tableOrderPayer2.getOrderItems().get(0),tableOrderPayer1, tableOrderPayer2.getOrderItems().get(0).getCount());
//        tableOrderPayer2.moveItemToAnotherPayer(tableOrderPayer2.getOrderItems().get(0),tableOrderPayer1, tableOrderPayer2.getOrderItems().get(0).getCount());
//        tableOrder.getPayment().deletePayer(tableOrderPayer2);
//        Assertions.assertEquals(1, payment1.getPayers().size());
//        tableOrder.getPayment().getPayers().get(0).makeBill(); // checking if it returns same result as line 201


        //6. Reset the payment to an initial status
        //   (cancelling all the activities like moving items, paying, tipping, and making/deleting payers..etc)

        Payment payment4 = Payment.createPayment(tableOrder, 3);
        tableOrder.getPayment().getPayers().get(0).makeBill(); //-> checking if it returns same result as line 201
        Assertions.assertNotSame(tableOrder.getPayment(), payment1);
        Assertions.assertNotSame(orderRepository.findOne(tableOrderId).getPayment(), payment1);

        Assertions.assertSame(tableOrder.getPayment(), payment4);
        Assertions.assertSame(orderRepository.findOne(tableOrderId).getPayment(), payment4);


        //7. cancelling order






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