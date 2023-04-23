package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
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
public class ItemRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void registerItem() throws Exception{
        //given
        User user1 = new User();
        user1.setEmail("gggabikr3@gmail.com");
        user1.setFullName("Table 003");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_OWNER);


        User user2 = new User();
        user2.setEmail("gggab4@gmail.com");
        user2.setFullName("Jacob Lee");
        user2.setPassword("abcdef");
        user2.setPhone("7787503982");
        user2.setRole(UserRole.ROLE_OWNER);


        userRepository.save(user1);
        userRepository.save(user2);


        Store store1 = new Store();
        store1.setOwner(user1);
        Address address = new Address("B.C", "Vancouver", "5321 donut Ave", "V6M 1Y4");
        store1.setAddress(address);
        store1.setPhone("7788021324");
        store1.setStoreDescription("aaaaaa");
        store1.setName("Jason's donuts");
        store1.setGratuity(5);
        store1.setGratuityPercent(15);
        store1.setStatus(false);
        store1.setRunningFlag(false);

        Store store2 = new Store();
        store2.setOwner(user2);
        Address address2 = new Address("B.C", "Vancouver", "9977 Maple St", "#305", "V1A 3C4");
        store2.setAddress(address2);
        store2.setPhone("6041234567");
        store2.setStoreDescription("aaaaaabb");
        store2.setName("Jacob's kitchen");
        store2.setGratuity(6);
        store2.setGratuityPercent(15);
        store2.setStatus(false);
        store1.setRunningFlag(false);

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
        Item item3 = new Item(store1, store1.getCategories().get(0),"Ramen", 13.70, "food");
        Item item4 = new Item(store1, store1.getCategories().get(1),"Coke", 2.50, "soda");
        Item item5 = new Item(store1, store1.getCategories().get(1),"Coffee", 4.50, "nonSoda");


        Item itemA = new Item(store2, store2.getCategories().get(0), "Rice noodle soup", 17.25, "food");
        Item itemB = new Item(store2, store2.getCategories().get(0), "LemonGrass chicken", 15.50, "food");
        Item itemC = new Item(store2, store2.getCategories().get(0), "Spring rolls", 6.55, "food");
        Item itemD = new Item(store2, store2.getCategories().get(0), "Spicy rice noodle soup", 18.25, "food");
        Item itemE = new Item(store2, store2.getCategories().get(0), "Cold brew coffee", 5.25, "nonSoda");
        Item itemF = new Item(store2, store2.getCategories().get(0), "Kokanee Beer", 4.25, "alcoholic");

        Item itemError1 = new Item(store1, store2.getCategories().get(0), "Error", 13.00, "food");

        //when
        itemRepository.registerItem(item1);
        itemRepository.registerItem(item2);
        itemRepository.registerItem(item3);
        itemRepository.registerItem(item4);
        itemRepository.registerItem(item5);
        itemRepository.registerItem(itemA);
        itemRepository.registerItem(itemB);
        itemRepository.registerItem(itemC);
        itemRepository.registerItem(itemD);
        itemRepository.registerItem(itemE);
        itemRepository.registerItem(itemF);


        //error should be raised
//        itemRepository.registerItem(itemError1);



        //then


    }

    @Test
    public void removeItem() {
    }

    @Test
    public void findOne() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findByStore() {
    }

    @Test
    public void findByStoreDeleted() {
    }

    @Test
    public void findByStoreAndCategory() {
    }

    @Test
    public void findByStoreAndCategoryDeleted() {
    }

    @Test
    public void findByItemType() {
    }

    @Test
    public void findByStoreAndItemType() {
    }

    @Test
    public void findByStoreAndItemTypeDeleted() {
    }

    @Test
    public void findByItemTag() {
    }

    @Test
    public void findByStatus() {
    }

    @Test
    public void findDiscountedItemOnStore() {
    }
}