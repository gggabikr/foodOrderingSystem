package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
        Item itemG = new Item(store2, store2.getCategories().get(0), "Coke", 2.25, "soda");

        Item itemError1 = new Item(store1, store2.getCategories().get(0), "Error", 13.00, "food");
        Item itemError2 = new Item(store1, store1.getCategories().get(0), "Ramen", 1110.00, "food");

        item1.setItemTag(ItemTag.Popular);
        item2.setItemTag(ItemTag.Popular);
        item4.setItemTag(ItemTag.Popular);
        itemA.setItemTag(ItemTag.Best);
        itemB.setItemTag(ItemTag.Recommended);
        itemC.setItemTag(ItemTag.Popular);
        itemD.setItemTag(ItemTag.Popular);



        //when
        Long item1Id = itemRepository.registerItem(item1);
        Long item2Id = itemRepository.registerItem(item2);
        itemRepository.registerItem(item3);
        itemRepository.registerItem(item4);
        itemRepository.registerItem(item5);
        itemRepository.registerItem(itemA);
        itemRepository.registerItem(itemB);
        itemRepository.registerItem(itemC);
        itemRepository.registerItem(itemD);
        itemRepository.registerItem(itemE);
        Long itemFId = itemRepository.registerItem(itemF);
        Long itemGId = itemRepository.registerItem(itemG);


        //error should be raised
//        itemRepository.registerItem(itemError1);
//        itemRepository.registerItem(itemError2);


        //then
        Assertions.assertEquals(item1, itemRepository.findOne(item1Id));
        Assertions.assertEquals(item2, itemRepository.findOne(item2Id));
        Assertions.assertEquals(itemF, itemRepository.findOne(itemFId));
        Assertions.assertEquals(itemG, itemRepository.findOne(itemGId));

        Assertions.assertEquals(12, itemRepository.findAll().size());

        Assertions.assertEquals(5, itemRepository.findByStore(store1).size());
        Assertions.assertEquals(7, itemRepository.findByStore(store2).size());

        Assertions.assertEquals(3, itemRepository.findByStoreAndCategory(store1, cate1).size());
        Assertions.assertEquals(2, itemRepository.findByStoreAndCategory(store1, cate2).size());
        Assertions.assertEquals(7, itemRepository.findByStoreAndCategory(store2, cat1).size());

        Assertions.assertEquals(7, itemRepository.findByItemType(ItemType.valueOf("food")).size());
        Assertions.assertEquals(2, itemRepository.findByItemType(ItemType.valueOf("soda")).size());
        Assertions.assertEquals(2, itemRepository.findByItemType(ItemType.valueOf("nonSoda")).size());
        Assertions.assertEquals(1, itemRepository.findByItemType(ItemType.valueOf("alcoholic")).size());

        Assertions.assertEquals(3, itemRepository.findByStoreAndItemType(store1, ItemType.valueOf("food")).size());
        Assertions.assertEquals(4, itemRepository.findByStoreAndItemType(store2, ItemType.valueOf("food")).size());
        Assertions.assertEquals(1, itemRepository.findByStoreAndItemType(store2, ItemType.valueOf("soda")).size());

        Assertions.assertEquals(5, itemRepository.findByItemTag(ItemTag.Popular).size());
        Assertions.assertEquals(1, itemRepository.findByItemTag(ItemTag.Best).size());
        Assertions.assertEquals(1, itemRepository.findByItemTag(ItemTag.Recommended).size());
        Assertions.assertEquals(5, itemRepository.findByItemTag(ItemTag.NoTag).size());

        Assertions.assertEquals(3, itemRepository.findByStoreAndItemTag(store1,ItemTag.Popular).size());
        Assertions.assertEquals(2, itemRepository.findByStoreAndItemTag(store1,ItemTag.NoTag).size());
        Assertions.assertEquals(0, itemRepository.findByStoreAndItemTag(store1,ItemTag.Best).size());

        Assertions.assertEquals(2, itemRepository.findByStoreAndItemTag(store2,ItemTag.Popular).size());
        Assertions.assertEquals(1, itemRepository.findByStoreAndItemTag(store2,ItemTag.Recommended).size());
        Assertions.assertEquals(1, itemRepository.findByStoreAndItemTag(store2,ItemTag.Best).size());
        Assertions.assertEquals(3, itemRepository.findByStoreAndItemTag(store2,ItemTag.NoTag).size());

        Assertions.assertEquals(12, itemRepository.findByStatus(true).size());
        Assertions.assertEquals(0, itemRepository.findByStatus(false).size());



        //when2
        itemRepository.removeItem(store1, item1);
        itemRepository.removeItem(store1, item5);

        itemRepository.removeItem(store2, itemC);
        itemRepository.removeItem(store2, itemE);
        itemRepository.removeItem(store2, itemF);

        item2.setDiscountPercent(50);
        item3.setDiscountAmount(3.7);
        itemB.setDiscountAmount(5.5);

        //then2
        Assertions.assertEquals(7, itemRepository.findAll().size());
        Assertions.assertEquals(5, itemRepository.findAllDeleted().size());

        Assertions.assertEquals(3, itemRepository.findByStore(store1).size());
        Assertions.assertEquals(2, itemRepository.findByStoreDeleted(store1).size());

        Assertions.assertEquals(4, itemRepository.findByStore(store2).size());
        Assertions.assertEquals(3, itemRepository.findByStoreDeleted(store2).size());

        Assertions.assertEquals(2, itemRepository.findByStoreAndCategory(store1, cate1).size());
        Assertions.assertEquals(1, itemRepository.findByStoreAndCategoryDeleted(store1, cate1).size());

        Assertions.assertEquals(1, itemRepository.findByStoreAndCategory(store1, cate2).size());
        Assertions.assertEquals(1, itemRepository.findByStoreAndCategoryDeleted(store1, cate2).size());

        Assertions.assertEquals(4, itemRepository.findByStoreAndCategory(store2, cat1).size());
        Assertions.assertEquals(3, itemRepository.findByStoreAndCategoryDeleted(store2, cat1).size());


        //7 food items -> 2 of them has deleted -> 5
        Assertions.assertEquals(5, itemRepository.findByItemType(ItemType.valueOf("food")).size());
        Assertions.assertEquals(2, itemRepository.findByItemType(ItemType.valueOf("soda")).size());
        //2 nonSoda items -> 2 of them has deleted ->0
        Assertions.assertEquals(0, itemRepository.findByItemType(ItemType.valueOf("nonSoda")).size());
        //1 alcoholic items -> 1 of them has deleted ->0
        Assertions.assertEquals(0, itemRepository.findByItemType(ItemType.valueOf("alcoholic")).size());

        Assertions.assertEquals(2, itemRepository.findByStoreAndItemType(store1, ItemType.valueOf("food")).size());
        Assertions.assertEquals(1, itemRepository.findByStoreAndItemTypeDeleted(store1, ItemType.valueOf("food")).size());

        Assertions.assertEquals(3, itemRepository.findByStoreAndItemType(store2, ItemType.valueOf("food")).size());
        Assertions.assertEquals(1, itemRepository.findByStoreAndItemTypeDeleted(store2, ItemType.valueOf("food")).size());

        Assertions.assertEquals(1, itemRepository.findByStoreAndItemType(store2, ItemType.valueOf("soda")).size());
        Assertions.assertEquals(0, itemRepository.findByStoreAndItemTypeDeleted(store2, ItemType.valueOf("soda")).size());

        Assertions.assertEquals(7, itemRepository.findByStatus(true).size());
        Assertions.assertEquals(0, itemRepository.findByStatus(false).size());

        Assertions.assertEquals(2, itemRepository.findDiscountedItemOnStore(store1).size());
        Assertions.assertEquals(1, itemRepository.findDiscountedItemOnStore(store2).size());

        Assertions.assertEquals(1, itemRepository.findByNameAndStore(store1, "curry").size());
        Assertions.assertEquals(1, itemRepository.findByNameAndStore(store1, "Curry").size());
        Assertions.assertEquals("Chicken Curry", itemRepository.findByNameAndStore(store1, "curry").get(0).getName());

        itemRepository.reRegisterItem(store1, cate1, item1);

        for(Item item: itemRepository.findByStore(store1)){
            System.out.println(item.getName());
        }

        Assertions.assertEquals(2, itemRepository.findByNameAndStore(store1, "Chicken").size());
        Assertions.assertEquals(2, itemRepository.findByNameAndStore(store2, "noodle soup").size());
        Assertions.assertEquals(2, itemRepository.findByNameAndStore(store2, "nOodLe soUP").size());

        Item item6 = new Item(store1, store1.getCategories().get(0), "Rice noodle soup", 10.25, "food");
        itemRepository.registerItem(item6);

        Assertions.assertEquals(2, itemRepository.findByNameAndStore(store2, "noodle soup").size());
        Assertions.assertEquals(1, itemRepository.findByNameAndStore(store1, "noodle soup").size());
        Assertions.assertEquals(10.25, itemRepository.findByNameAndStore(store1, "noodle soup").get(0).getPrice());

    }
}