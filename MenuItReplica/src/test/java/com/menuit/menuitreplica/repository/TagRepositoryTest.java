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
public class TagRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    TagRepository tagRepository;

    @Test
    public void create() throws Exception {
        //given
        Tag tag1 = new Tag();
        tag1.setName("Chinese");
        Tag tag2 = new Tag();
        tag2.setName("Korean");
        Tag tag3 = new Tag();
        tag3.setName("Greek");
        Tag tag4 = new Tag();
        tag4.setName("France");
        Tag tag5 = new Tag();
        tag5.setName("American");

        //when
        Long tag1Id = tagRepository.create(tag1);
        Long tag2Id = tagRepository.create(tag2);
        Long tag3Id = tagRepository.create(tag3);
        Long tag4Id = tagRepository.create(tag4);
        Long tag5Id = tagRepository.create(tag5);

        //then
        Assertions.assertEquals(tag1, tagRepository.findOne(tag1Id));
        Assertions.assertEquals(tag2, tagRepository.findOne(tag2Id));
        Assertions.assertEquals(tag3, tagRepository.findOne(tag3Id));
        Assertions.assertEquals(tag4, tagRepository.findOne(tag4Id));
        Assertions.assertEquals(tag5, tagRepository.findOne(tag5Id));


//        User user1 = new User();
//        user1.setEmail("gggabikr3@gmail.com");
//        user1.setFullName("Table 003");
//        user1.setPassword("aabbccde");
//        user1.setPhone("7788097503");
//        user1.setRole(UserRole.ROLE_OWNER);
//
//        userRepository.save(user1);
//
//
//        Store store1 = new Store();
//        store1.setOwner(user1);
//        Address address = new Address("B.C", "Vancouver", "5321 donut Ave", "V6M 1Y4");
//        store1.setAddress(address);
//        store1.setPhone("7788021324");
//        store1.setStoreDescription("aaaaaa");
//        store1.setName("Jason's donuts");
//        store1.setGratuity(5);
//        store1.setGratuityPercent(15);
//        store1.setStatus(false);
//        store1.setRunningFlag(false);
//
//        Store store2 = new Store();
//        store2.setOwner(user1);
//        Address address2 = new Address("B.C", "Vancouver", "9977 Maple St", "#305", "V1A 3C4");
//        store2.setAddress(address2);
//        store2.setPhone("6041234567");
//        store2.setStoreDescription("aaaaaabb");
//        store2.setName("Jacob's kitchen");
//        store2.setGratuity(6);
//        store2.setGratuityPercent(15);
//        store2.setStatus(false);
//        store1.setRunningFlag(false);
    }

    @Test
    public void findOne() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findByExactName() {
    }

    @Test
    public void findByNameContaining() {
    }

    @Test
    public void deleteTag() {
    }
}