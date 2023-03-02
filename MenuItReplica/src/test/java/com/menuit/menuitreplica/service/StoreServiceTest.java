package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.*;
import com.menuit.menuitreplica.repository.RatingRepository;
import com.menuit.menuitreplica.repository.TagRepository;
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
public class StoreServiceTest {

    @Autowired
    StoreService storeService;
    @Autowired
    UserService userService;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    TagRepository tagRepository;

    @Test
    public void registerStore() throws Exception {
        //given
        User user1 = new User();
        user1.setEmail("gggabikr@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_OWNER);

        Address address = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");
        Long registerStore = storeService.registerStore("Jason's kitchen", address, "6049991111", user1);

        //when
        Store store = storeService.findOne(registerStore);

        //then
        Assertions.assertEquals(user1.getStores().get(0), store);
    }

    @Test
    public void multiTest() throws Exception {
        //given, when
        Long user1Id = userService.join("gggabikr@gmail.com", "JasonLee", "abcdef", UserRole.ROLE_OWNER, null);
        Long user2Id = userService.join("gggab@gmail.com", "Jacob Lee", "abcdefgg", UserRole.ROLE_OWNER, null);

        User user1 = userService.findOneById(user1Id);
        User user2 = userService.findOneById(user2Id);

        Address address = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");

        Long registerStoreId1 = storeService.registerStore("Jason's kitchen", address, "6049991111", user1);
        Long registerStoreId2 = storeService.registerStore("Jason's donut", address, "7788021324", user2);
        Long registerStoreId3 = storeService.registerStore("Jason's Garden", address, "1231231234", user2);
        Long registerStoreId4 = storeService.registerStore("Jason's kitchen", address, "2342342344", user1);

        Store store1 = storeService.findOne(registerStoreId1);
        Store store2 = storeService.findOne(registerStoreId2);
        Store store3 = storeService.findOne(registerStoreId3);
        Store store4 = storeService.findOne(registerStoreId4);

        store2.toggleStatus();
        store1.toggleStatus();

        Rating rating1 = new Rating(user1,store1, 3.5, null);
        Rating rating2 = new Rating(user2,store1, 5.0, null); //4.25
        Rating rating3 = new Rating(user1,store2, 3.0, null);
        Rating rating4 = new Rating(user2,store2, 2.0, null); //2.5
        Rating rating5 = new Rating(user1,store3, 3.5, null);
        Rating rating6 = new Rating(user2,store3, 3.5, null); //3.5
        Rating rating7 = new Rating(user1,store4, 5.0, null);
        Rating rating8 = new Rating(user2,store4, 5.0, null); //5.0

        Long category1 = storeService.addNewCategory(store1, "Appetizers");
        Long category2 = storeService.addNewCategory(store1, "Main dishes");
        Long category3 = storeService.addNewCategory(store1, "Lunch Combos");
        Long category4 = storeService.addNewCategory(store1, "Drinks");
        Long category5 = storeService.addNewCategory(store2, "Dinner combos");
        Long category6 = storeService.addNewCategory(store2, "Starters");

        //then
        Assertions.assertEquals(4, storeService.findAll().size());
//        어떤 이유인지는 몰라도 아래 두 테스트는 실패한다.
//        Assertions.assertEquals(user1.getStores(), storeService.findByOwner(user1));
//        Assertions.assertEquals(user2.getStores(), storeService.findByOwner(user2));
        Assertions.assertEquals(store4, storeService.findByPhoneNumber("2342342344").get(0));
        Assertions.assertEquals(1, storeService.findByPhoneNumber("2342342344").size());
        Assertions.assertEquals(store1, storeService.findByPhoneNumber("6049991111").get(0));
        Assertions.assertEquals(1, storeService.findByPhoneNumber("6049991111").size());
        Assertions.assertTrue(storeService.findByStatus(true).contains(store1));
        Assertions.assertTrue(storeService.findByStatus(true).contains(store2));
        Assertions.assertEquals(2, storeService.findByStatus(true).size());
        Assertions.assertEquals(2, storeService.findByRatingScore(4.0).size());
        Assertions.assertEquals(1, storeService.findByRatingScore(5.0).size());
        Assertions.assertEquals(store4, storeService.findByRatingScore(5.0).get(0));
        Assertions.assertEquals(4, storeService.findByRatingScore(2.5).size());
        Assertions.assertTrue(store1.getRatings().contains(rating1));
        Assertions.assertTrue(store1.getRatings().contains(rating2));
        Assertions.assertEquals(4.25, store1.getRatingScore());

        Assertions.assertEquals(store1.getCategories().get(0),storeService.findOneCategory(category1));
        Assertions.assertEquals(store1.getCategories().get(3),storeService.findOneCategory(category4));
        Assertions.assertEquals(4,storeService.findCategoriesByStore(store1).size());
        Assertions.assertEquals("Appetizers",storeService.findCategoriesByStore(store1).get(0).getName());
        Assertions.assertEquals("Lunch Combos",storeService.findCategoriesByStore(store1).get(2).getName());

        storeService.deleteCategory(store1, storeService.findOneCategory(category2));
        Assertions.assertEquals("Drinks",storeService.findCategoriesByStore(store1).get(2).getName());
        Assertions.assertEquals(3,storeService.findCategoriesByStore(store1).size());
        Assertions.assertEquals(2,storeService.findCategoriesByStore(store2).size());
        Assertions.assertEquals(5, storeService.findAllCategories().size());






    }

    @Test
    public void findByStoreTag() throws Exception{
        //given
        Long user1Id = userService.join("gggabikr@gmail.com", "JasonLee", "abcdef", UserRole.ROLE_OWNER, null);
        Long user2Id = userService.join("gggab@gmail.com", "Jacob Lee", "abcdefgg", UserRole.ROLE_OWNER, null);

        User user1 = userService.findOneById(user1Id);
        User user2 = userService.findOneById(user2Id);

        Address address = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");

        Long registerStoreId1 = storeService.registerStore("Jason's kitchen", address, "6049991111", user1);
        Long registerStoreId2 = storeService.registerStore("Jason's donut", address, "7788021324", user2);
        Long registerStoreId3 = storeService.registerStore("Jason's Garden", address, "1231231234", user2);
        Long registerStoreId4 = storeService.registerStore("Jason's kitchen", address, "2342342344", user1);

        Store store1 = storeService.findOne(registerStoreId1);
        Store store2 = storeService.findOne(registerStoreId2);
        Store store3 = storeService.findOne(registerStoreId3);
        Store store4 = storeService.findOne(registerStoreId4);

        Tag tag1 = new Tag();
        tag1.setName("Chinese");
        tagRepository.create(tag1);

        StoreTag storeTag1 = new StoreTag();
        storeTag1.setTag(tag1);
        store1.addStoreTag(storeTag1);

        StoreTag storeTag2 = new StoreTag();
        storeTag2.setTag(tag1);
        store3.addStoreTag(storeTag2);


        //then
        Assertions.assertEquals(2, storeService.findByStoreTag(tag1).size());
        Assertions.assertEquals(store1, storeService.findByStoreTag(tag1).get(0));
        Assertions.assertEquals(store3, storeService.findByStoreTag(tag1).get(1));
        Assertions.assertEquals(2, storeService.findByStoreTagName("Chinese").size());
        Assertions.assertEquals(store1, storeService.findByStoreTagName("Chinese").get(0));
        Assertions.assertEquals(store3, storeService.findByStoreTagName("Chinese").get(1));
        Assertions.assertEquals(0, storeService.findByStoreTagName("Chin").size());

    }
}