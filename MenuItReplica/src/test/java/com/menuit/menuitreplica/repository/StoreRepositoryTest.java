package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StoreRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    TagRepository tagRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void multipleTests() throws Exception {

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

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        storeRepository.register(store1);
        storeRepository.register(store2);


            //test for findByOwner
        List<Store> byOwner1 = storeRepository.findByOwner(user1);
        List<Store> byOwner2 = storeRepository.findByOwner(user2);

            //test for findOne
        Store foundStoreById1 = storeRepository.findOne(store1.getId());
        Store foundStoreById2 = storeRepository.findOne(store2.getId());


            //test for findByPhoneNumber
        List<Store> byPhoneNumber1 = storeRepository.findByPhoneNumber("7788021324");
        List<Store> byPhoneNumber2 = storeRepository.findByPhoneNumber("6041234567");

            //test for findAll
        List<Store> all = storeRepository.findAll();

            //test for findByStatus
        List<Store> byStatusTrue = storeRepository.findByStatus(true);
        List<Store> byStatusFalse = storeRepository.findByStatus(false);

            //test for categories
        Long category1 = storeRepository.addNewCategory(store1, "Appetizers");
        Long category2 = storeRepository.addNewCategory(store1, "Main dishes");
        Long category3 = storeRepository.addNewCategory(store1, "Lunch Combos");
        Long category4 = storeRepository.addNewCategory(store1, "Drinks");
        Long category5 = storeRepository.addNewCategory(store2, "Dinner combos");
        Long category6 = storeRepository.addNewCategory(store2, "Starters");

        //then
        Assertions.assertEquals(store1.getId(), byOwner1.get(0).getId());
        Assertions.assertEquals(store1, foundStoreById1);
        Assertions.assertEquals(store2.getId(), byOwner2.get(0).getId());
        Assertions.assertEquals(store2, foundStoreById2);
        Assertions.assertTrue(all.contains(store1));
        Assertions.assertTrue(all.contains(store2));
        Assertions.assertEquals(2, all.size());
        Assertions.assertTrue(byPhoneNumber1.contains(store1));
        Assertions.assertTrue(byPhoneNumber2.contains(store2));
        Assertions.assertFalse(byPhoneNumber1.contains(store2));
        Assertions.assertEquals(byPhoneNumber1.get(0), byStatusTrue.get(0));
        Assertions.assertEquals(byPhoneNumber2.get(0), byStatusFalse.get(0));
        Assertions.assertEquals(store1.getCategories().get(0),storeRepository.findOneCategory(category1));
        Assertions.assertEquals(store1.getCategories().get(3),storeRepository.findOneCategory(category4));
        Assertions.assertEquals(4,storeRepository.findCategoriesByStore(store1).size());
        Assertions.assertEquals("Appetizers",storeRepository.findCategoriesByStore(store1).get(0).getName());
        Assertions.assertEquals("Lunch Combos",storeRepository.findCategoriesByStore(store1).get(2).getName());

        storeRepository.deleteCategory(store1, storeRepository.findOneCategory(category2));
        Assertions.assertEquals("Drinks",storeRepository.findCategoriesByStore(store1).get(2).getName());
        Assertions.assertEquals(3,storeRepository.findCategoriesByStore(store1).size());
        Assertions.assertEquals(2,storeRepository.findCategoriesByStore(store2).size());
        Assertions.assertEquals(5, storeRepository.findAllCategories().size());



    }


//    @Test
//    public void findByStoreTag() {
//    }

    @Test
    public void findByRatingScore() throws Exception {
        //given
        User user1 = new User();
        user1.setEmail("gggabikr@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_OWNER);

        User user2 = new User();
        user2.setEmail("gggab@gmail.com");
        user2.setFullName("Jacob Lee");
        user2.setPassword("abcdef");
        user2.setPhone("7787503982");
        user2.setRole(UserRole.ROLE_GENERAL);

        Address address = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");

        Store store1 = new Store();
        store1.setOwner(user1);
        store1.setAddress(address);
        store1.setPhone("7788021324");
        store1.setStoreDescription("aaaaaa");
        store1.setName("Jason's donuts");
        store1.setStatus(true);

        Store store2 = new Store();
        store2.setOwner(user1);
        store2.setAddress(address);
        store2.setPhone("6041234567");
        store2.setStoreDescription("aaaaaabb");
        store2.setName("Jason's kitchen");
        store2.setStatus(false);

        Store store3 = new Store();
        store3.setOwner(user1);
        store3.setAddress(address);
        store3.setPhone("6041232222");
        store3.setStoreDescription("aaaaaabbc");
        store3.setName("Jason's Garden");
        store3.setStatus(true);

        Rating rating1 = new Rating(user1, store1, 3, null);
        Rating rating2 = new Rating(user1, store2, 4, null);
        Rating rating3 = new Rating(user2, store1, 2, null);
        Rating rating4 = new Rating(user2, store2, 5, null);


        //when
        userRepository.save(user1);
        storeRepository.register(store1);
        storeRepository.register(store2);

        System.out.println(store1.getRatingScore());
        System.out.println(store2.getRatingScore());
        System.out.println(store3.getRatingScore());


        List<Store> above100 = storeRepository.findByRatingScore(1);
        List<Store> above150 = storeRepository.findByRatingScore(1.5);
        List<Store> above200 = storeRepository.findByRatingScore(2);
        List<Store> above250 = storeRepository.findByRatingScore(2.5);
        List<Store> above251 = storeRepository.findByRatingScore(2.51);
        List<Store> above300 = storeRepository.findByRatingScore(3);
        List<Store> above350 = storeRepository.findByRatingScore(3.5);
        List<Store> above400 = storeRepository.findByRatingScore(4);
        List<Store> above430 = storeRepository.findByRatingScore(4.3);
        List<Store> above450 = storeRepository.findByRatingScore(4.5);
        List<Store> above480 = storeRepository.findByRatingScore(4.8);
        List<Store> above500 = storeRepository.findByRatingScore(5);


        //then
        Assertions.assertEquals(2, above100.size());
        Assertions.assertEquals(2, above150.size());
        Assertions.assertEquals(2, above200.size());
        Assertions.assertEquals(2, above250.size());
        Assertions.assertTrue(above250.contains(store1));
        Assertions.assertTrue(above250.contains(store2));
        Assertions.assertFalse(above250.contains(store3));

        Assertions.assertEquals(1, above251.size());
        Assertions.assertEquals(1, above300.size());
        Assertions.assertEquals(1, above350.size());
        Assertions.assertEquals(1, above400.size());
        Assertions.assertEquals(1, above430.size());
        Assertions.assertEquals(1, above450.size());
        Assertions.assertTrue(above450.contains(store2));
        Assertions.assertFalse(above450.contains(store1));
        Assertions.assertFalse(above450.contains(store3));

        Assertions.assertEquals(0, above480.size());
        Assertions.assertEquals(0, above500.size());

        //test if ratings are saved user's ratings list
        Assertions.assertEquals(2, user1.getRatings().size());
        Assertions.assertTrue(user1.getRatings().contains(rating1));
        Assertions.assertTrue(user1.getRatings().contains(rating2));
        Assertions.assertFalse(user1.getRatings().contains(rating3));
        Assertions.assertFalse(user1.getRatings().contains(rating4));

        Assertions.assertEquals(2, user2.getRatings().size());
        Assertions.assertTrue(user2.getRatings().contains(rating3));
        Assertions.assertTrue(user2.getRatings().contains(rating4));
        Assertions.assertFalse(user2.getRatings().contains(rating1));
        Assertions.assertFalse(user2.getRatings().contains(rating2));

    }
//    왠지 모르겠지만 아래테스트(같은 유저가 같은 스토어에 리뷰를 두개이상 남길경우 오류가 발생하는지에 대한 테스트)가
//    작동을 정상적으로 하지 않는다. 서비스단에서 중복체크후 에러발생시키는 방식으로 변경.
//    @Test(expected = DataIntegrityViolationException.class)
//    @Rollback(false)
////    @Transactional
//    public void twoRatingsToOneStore() throws Exception{
//        //given
//        User user1 = new User();
//        user1.setEmail("gggabikr@gmail.com");
//        user1.setFullName("Jason Lee");
//        user1.setPassword("aabbccde");
//        user1.setPhone("7788097503");
//        user1.setRole(UserRole.ROLE_OWNER);
//
//        Address address = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");
//
//        Store store1 = new Store();
//        store1.setOwner(user1);
//        store1.setAddress(address);
//        store1.setPhone("7788021324");
//        store1.setStoreDescription("aaaaaa");
//        store1.setName("Jason's donuts");
//        store1.setStatus(true);
//
//        Store store2 = new Store();
//        store2.setOwner(user1);
//        store2.setAddress(address);
//        store2.setPhone("6041234567");
//        store2.setStoreDescription("aaaaaabb");
//        store2.setName("Jason's kitchen");
//        store2.setStatus(false);
//
//        //when
//        userRepository.save(user1);
//        storeRepository.register(store1);
//        storeRepository.register(store2);
//
//        Rating rating1 = new Rating(user1, store1, 3.0, null);
//        Rating rating2 = new Rating(user1, store2, 4.0, null);
//        Rating rating3 = new Rating(user1, store1, 2.0, null);
//
//        ratingRepository.save(rating1);
//        ratingRepository.save(rating2);
//        ratingRepository.save(rating3);
//
//        //then
//        Assertions.fail("This shouldn't be run.");
//    }

    @Test
    public void storeTagTests() throws Exception{
        //given
        User user1 = new User();
        user1.setEmail("gggabikr@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_OWNER);

        User user2 = new User();
        user2.setEmail("gggab@gmail.com");
        user2.setFullName("Jacob Lee");
        user2.setPassword("abcdef");
        user2.setPhone("7787503982");
        user2.setRole(UserRole.ROLE_GENERAL);

        Address address = new Address("B.C", "Vancouver", "1990 41st Ave", "#303", "V6M 1Y4");

        Store store1 = new Store();
        store1.setOwner(user1);
        store1.setAddress(address);
        store1.setPhone("7788021324");
        store1.setStoreDescription("aaaaaa");
        store1.setName("Jason's donuts");
        store1.setStatus(true);

        Store store2 = new Store();
        store2.setOwner(user1);
        store2.setAddress(address);
        store2.setPhone("6041234567");
        store2.setStoreDescription("aaaaaabb");
        store2.setName("Jason's kitchen");
        store2.setStatus(false);

        Store store3 = new Store();
        store3.setOwner(user1);
        store3.setAddress(address);
        store3.setPhone("6041232222");
        store3.setStoreDescription("aaaaaabbc");
        store3.setName("Jason's Garden");
        store3.setStatus(true);

        //when
        userRepository.save(user1);
        storeRepository.register(store1);
        storeRepository.register(store2);
        storeRepository.register(store3);


        Tag tag1 = new Tag();
        tag1.setName("Chinese");

        Tag tag2 = new Tag();
        tag1.setName("Korean");

        tagRepository.create(tag1);
        tagRepository.create(tag2);

        store1.addStoreTag(tag1);
        store1.addStoreTag(tag2);
        store2.addStoreTag(tag1);
        store3.addStoreTag(tag2);

        List<StoreTag> storeTagsByStore1 = storeRepository.findStoreTagsByStore(store1);
        List<StoreTag> storeTagsByStore2 = storeRepository.findStoreTagsByStore(store2);
        List<StoreTag> storeTagsByStore3 = storeRepository.findStoreTagsByStore(store3);

        List<Store> storesByTag1 = storeRepository.findByTag(tag1);
        List<Store> storesByTag2 = storeRepository.findByTag(tag2);

        //then
        Assertions.assertEquals(2, storeTagsByStore1.size());
        Assertions.assertEquals(1, storeTagsByStore2.size());
        Assertions.assertEquals(1, storeTagsByStore3.size());

        for(StoreTag storeTag: storeTagsByStore1){
            Assertions.assertEquals(storeTag.getStore(), store1);
        }
        for(StoreTag storeTag: storeTagsByStore2){
            Assertions.assertEquals(storeTag.getStore(), store2);
        }
        for(StoreTag storeTag: storeTagsByStore3){
            Assertions.assertEquals(storeTag.getStore(), store3);
        }
        Assertions.assertEquals(2, storesByTag1.size());
        Assertions.assertEquals(2, storesByTag2.size());
        Assertions.assertTrue(storesByTag1.contains(store1));
        Assertions.assertTrue(storesByTag1.contains(store2));
        Assertions.assertTrue(storesByTag2.contains(store1));
        Assertions.assertTrue(storesByTag2.contains(store3));
        Assertions.assertEquals(4, storeRepository.findAllStoreTags().size());

        storeRepository.deleteStoreTagByStoreAndTag(store1, tag1);
        Assertions.assertEquals(1, storeRepository.findStoreTagsByStore(store1).size());
        Assertions.assertEquals(1, store1.getTags().size());
        Assertions.assertEquals(store1.getTags().get(0).getTag(), tag2);
    }
}