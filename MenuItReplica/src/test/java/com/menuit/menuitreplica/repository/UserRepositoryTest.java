package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.User;
import com.menuit.menuitreplica.domain.UserRole;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void join() throws Exception{
        //given
        User user = new User();
        user.setEmail("gggabikr@gmail.com");
        user.setFullName("Jason Lee");
        user.setPassword("aabbccde");
        user.setPhone("7788097503");
        user.setRole(UserRole.ROLE_GENERAL);

        //when
        Long savedUserId = userRepository.save(user);
        User foundUser = userRepository.findOne(savedUserId);

        //then
        Assertions.assertEquals(foundUser.getId(), user.getId());
        Assertions.assertEquals(foundUser.getFullName(), user.getFullName());
        Assertions.assertEquals(foundUser, user);


    }

    @Test(expected = IllegalStateException.class)
    public void duplicatedEmail() throws Exception{
        //given
        User user1 = new User();
        user1.setEmail("gggabikr@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_GENERAL);

        User user2 = new User();
        user2.setEmail("gggabikr@gmail.com");
        user2.setFullName("Justin Lee");
        user2.setPassword("aabbccdeeee");
        user2.setPhone("7780000750");
        user2.setRole(UserRole.ROLE_GENERAL);

        //when
        userRepository.save(user1);
        em.flush();
        userRepository.save(user2);

        //then
        Assertions.fail("error need to be raised");


    }

    @Test
    public void findAll() throws Exception{
        //given
        User user1 = new User();
        user1.setEmail("gggabikr1@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_GENERAL);

        User user2 = new User();
        user2.setEmail("gggabikr2@gmail.com");
        user2.setFullName("Justin Lee");
        user2.setPassword("aabbccdeeee");
        user2.setPhone("7780000750");
        user2.setRole(UserRole.ROLE_GENERAL);

        User user3 = new User();
        user3.setEmail("gggabikr3@gmail.com");
        user3.setFullName("James Lee");
        user3.setPassword("aabbccdeeee");
        user3.setPhone("7780007508");
        user3.setRole(UserRole.ROLE_GENERAL);
        //when
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        List<User> all = userRepository.findAll();

        //then
        Assertions.assertEquals(users, all);
        Assertions.assertEquals(users.get(0).getEmail(), all.get(0).getEmail());
        Assertions.assertTrue(all.contains(user3));
        Assertions.assertEquals(3, all.size());


    }

    @Test
    public void findByEmail() throws Exception {
        //given
        User user1 = new User();
        user1.setEmail("gggabikr1@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_GENERAL);

        User user2 = new User();
        user2.setEmail("gggabikr2@gmail.com");
        user2.setFullName("Justin Lee");
        user2.setPassword("aabbccdeeee");
        user2.setPhone("7780000750");
        user2.setRole(UserRole.ROLE_GENERAL);

        User user3 = new User();
        user3.setEmail("gggabikr3@gmail.com");
        user3.setFullName("James Lee");
        user3.setPassword("aabbccdeeee");
        user3.setPhone("7780007508");
        user3.setRole(UserRole.ROLE_GENERAL);

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        //then
        List<User> foundByEmail = userRepository.findByEmail("gggabikr2@gmail.com");
        List<User> foundByEmail2 = userRepository.findByEmail("gggabikr@gmail.com");

        Assertions.assertEquals(user2, foundByEmail.get(0));
        Assertions.assertEquals(1, foundByEmail.size());
        Assertions.assertEquals(0, foundByEmail2.size());
    }

    @Test
    public void findByUserRole() throws Exception {
        //given
        User user1 = new User();
        user1.setEmail("gggabikr1@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_GENERAL);

        User user2 = new User();
        user2.setEmail("gggabikr2@gmail.com");
        user2.setFullName("Justin Lee");
        user2.setPassword("aabbccdeeee");
        user2.setPhone("7780000750");
        user2.setRole(UserRole.ROLE_OWNER);

        User user3 = new User();
        user3.setEmail("gggabikr3@gmail.com");
        user3.setFullName("James Lee");
        user3.setPassword("aabbccdeeee");
        user3.setPhone("7780007508");
        user3.setRole(UserRole.ROLE_GENERAL);

        User user4 = new User();
        user4.setEmail("gggabikr4@gmail.com");
        user4.setFullName("Jay Lee");
        user4.setPassword("aabbcee");
        user4.setPhone("7780007508");
        user4.setRole(UserRole.ROLE_OWNER);

        User user5 = new User();
        user5.setEmail("gggabikr5@gmail.com");
        user5.setFullName("John Lee");
        user5.setPassword("aa3bcee");
        user5.setPhone("7780000883");
        user5.setRole(UserRole.ROLE_TABLE);

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        List<User> role_table = userRepository.findByUserRole(UserRole.ROLE_TABLE);
        List<User> role_owner = userRepository.findByUserRole(UserRole.ROLE_OWNER);
        List<User> role_general = userRepository.findByUserRole(UserRole.ROLE_GENERAL);

        //then
        Assertions.assertEquals(2, role_owner.size());
        Assertions.assertEquals(user5, role_table.get(0));
        Assertions.assertEquals(2, role_general.size());
        Assertions.assertTrue(role_general.contains(user1));
        Assertions.assertTrue(role_general.contains(user3));

    }

    @Test
    public void findByPhoneNumber() throws Exception {
        //given
        User user1 = new User();
        user1.setEmail("gggabikr1@gmail.com");
        user1.setFullName("Jason Lee");
        user1.setPassword("aabbccde");
        user1.setPhone("7788097503");
        user1.setRole(UserRole.ROLE_GENERAL);

        User user2 = new User();
        user2.setEmail("gggabikr2@gmail.com");
        user2.setFullName("Justin Lee");
        user2.setPassword("aabbccdeeee");
        user2.setPhone("7780000750");
        user2.setRole(UserRole.ROLE_OWNER);

        User user3 = new User();
        user3.setEmail("gggabikr3@gmail.com");
        user3.setFullName("James Lee");
        user3.setPassword("aabbccdeeee");
        user3.setPhone("7780007508");
        user3.setRole(UserRole.ROLE_GENERAL);

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Assertions.assertEquals(user1, userRepository.findByPhoneNumber("7788097503").get(0));
        Assertions.assertEquals(user2, userRepository.findByPhoneNumber("7780000750").get(0));
        Assertions.assertEquals(user3, userRepository.findByPhoneNumber("7780007508").get(0));
        Assertions.assertEquals(0, userRepository.findByPhoneNumber("7780001111").size());
        Assertions.assertEquals(0, userRepository.findByPhoneNumber("12341111").size());
    }
}