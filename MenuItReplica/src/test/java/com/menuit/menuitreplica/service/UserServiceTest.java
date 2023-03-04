package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.User;
import com.menuit.menuitreplica.domain.UserRole;
import com.menuit.menuitreplica.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void JoinUser() throws Exception{
        //given
        //when
        Long savedId = userService.join("gggabikr@gmail.com", "Jason Lee","aaabbcs", "ROLE_GENERAL",null);

        //then
        assertEquals(savedId, userRepository.findByEmail("gggabikr@gmail.com").get(0).getId());
        assertEquals(savedId, userRepository.findOne(savedId).getId());

    }

    @Test(expected = IllegalStateException.class)
    public void exceptionForDuplicated() throws Exception{

        //given
        //when

        //for the below code, error should not be arisen -> fail() method need to be worked
//            userService.join("breece@gmail.com","Breece Park","bbbddd", UserRole.ROLE_GENERAL);
//            userService.join("breec@gmail.com","Breece Pak","cccwww", UserRole.ROLE_GENERAL);


        userService.join("breece@gmail.com","Breece Park","bbbddd", "ROLE_GENERAL", null);
        userService.join("breece@gmail.com","Breece Pak","cccwww", "ROLE_GENERAL", null); //error should be arisen

        //then
        fail("error must be arisen");
    }


    @Test
    public void login() throws Exception{
        //given
        userService.join("gggabikr@gmail.com","JasonLee", "abcdef", "ROLE_GENERAL", null);
        userService.join("gggab@gmail.com","Jacob Lee", "abcdefgg", "ROLE_GENERAL", null);


        //when
        Boolean loginSuccess = userService.login("gggabikr@gmail.com", "abcdef");
        Boolean loginFail1 = userService.login("gggabikr@gmail.com", "abcdee");
        Boolean loginFail2 = userService.login("gggabikr@gmail.com", "abcdeF");
        Boolean loginFail3 = userService.login("gggabikr@gmail.com", "abcDef");
        Boolean loginFail4 = userService.login("gggabikr@gmail.com", "ABCDEF");
        Boolean loginFail5 = userService.login("gggabikr@gmail.com", "abcde!");
        Boolean loginFail6 = userService.login("gggabikr@gmail.com", "abcde.");
        Boolean loginFail7 = userService.login("gggabikr@gmail.com", "ab_def");
        Boolean loginFail8 = userService.login("gggabikr@gmail.com", "abcdefg");
        Boolean loginFail9 = userService.login("gggabikr@gmail.com", "ab#cdef");
        Boolean loginFail10 = userService.login("gggabikr@gmail.com", "abcd@f");

        Boolean loginSuccess2 = userService.login("gggab@gmail.com", "abcdefgg");
        Boolean loginFail11 = userService.login("gggab@gmail.com", "abcdeegg");
        Boolean loginFail12 = userService.login("gggab@gmail.com", "abcdeFgg");
        Boolean loginFail13 = userService.login("gggab@gmail.com", "abcDefgg");
        Boolean loginFail14 = userService.login("gggab@gmail.com", "ABCDEFGg");
        Boolean loginFail15 = userService.login("gggab@gmail.com", "abcde!gG");
        Boolean loginFail16 = userService.login("gggab@gmail.com", "abcde.gG");
        Boolean loginFail17 = userService.login("gggab@gmail.com", "ab_defGG");
        Boolean loginFail18 = userService.login("gggab@gmail.com", "abcdeFgG");
        Boolean loginFail19 = userService.login("gggab@gmail.com", "ab#cdef");
        Boolean loginFail20 = userService.login("gggab@gmail.com", "abcd@fg");


//            //then
        Assertions.assertEquals(true, loginSuccess);
        Assertions.assertEquals(false, loginFail1);
        Assertions.assertEquals(false, loginFail2);
        Assertions.assertEquals(false, loginFail3);
        Assertions.assertEquals(false, loginFail4);
        Assertions.assertEquals(false, loginFail5);
        Assertions.assertEquals(false, loginFail6);
        Assertions.assertEquals(false, loginFail7);
        Assertions.assertEquals(false, loginFail8);
        Assertions.assertEquals(false, loginFail9);
        Assertions.assertEquals(false, loginFail10);

        Assertions.assertEquals(true, loginSuccess2);
        Assertions.assertEquals(false, loginFail11);
        Assertions.assertEquals(false, loginFail12);
        Assertions.assertEquals(false, loginFail13);
        Assertions.assertEquals(false, loginFail14);
        Assertions.assertEquals(false, loginFail15);
        Assertions.assertEquals(false, loginFail16);
        Assertions.assertEquals(false, loginFail17);
        Assertions.assertEquals(false, loginFail18);
        Assertions.assertEquals(false, loginFail19);
        Assertions.assertEquals(false, loginFail20);
    }
//
    @Test
    public void changePassword() throws Exception{
        //given
        Long memberId = userService.join("gggabikr@gmail.com", "JasonLee", "abcdefG", "ROLE_GENERAL", null);
        Assertions.assertEquals(true, userService.login("gggabikr@gmail.com", "abcdefG"));


        //when
        Long memberIdAfterChangePassword = userService.changePassword(memberId, "abcccd1");

        //then
        Assertions.assertEquals(false, userService.login("gggabikr@gmail.com", "abcdefG"));
        Assertions.assertEquals(true, userService.login("gggabikr@gmail.com", "abcccd1"));
        Assertions.assertEquals(memberId, memberIdAfterChangePassword);

    }

    @Test
    public void findAllMembers() throws Exception{
        //given
        userService.join("ggg1@gmail.com","Jason Lee", "aabbccd", "ROLE_GENERAL", null);
        userService.join("ggg2@gmail.com","Breece Park", "ssddff", "ROLE_GENERAL", null);
        userService.join("ggg3@gmail.com","Daniel Lee", "ffgghh", "ROLE_GENERAL", null);

        //when
        List<User> allUsers = userService.findAllUsers();

        //then
        Assertions.assertEquals("ggg1@gmail.com", allUsers.get(0).getEmail());
        Assertions.assertEquals("ggg2@gmail.com", allUsers.get(1).getEmail());
        Assertions.assertEquals("ggg3@gmail.com", allUsers.get(2).getEmail());
        Assertions.assertEquals(3, allUsers.size());
    }


    @Test
    public void findByEmail() throws Exception {
        //given
        userService.join("gggabikr1@gmail.com", "Jason Lee", "aabbccde", "ROLE_GENERAL", "7788097503");
        Long user2Id = userService.join("gggabikr2@gmail.com", "Justin Lee", "aabbccdeeee", "ROLE_GENERAL", "7780000750");
        Long user3Id = userService.join("gggabikr3@gmail.com", "James Lee", "aabbccdeeee","ROLE_GENERAL", "7780007508");


        //when
        List<User> foundByEmail = userService.findByEmail("gggabikr@gmail.com");
        List<User> foundByEmail1 = userService.findByEmail("gggabikr1@gmail.com");
        List<User> foundByEmail2 = userService.findByEmail("gggabikr2@gmail.com");
        List<User> foundByEmail3 = userService.findByEmail("gggabikr3@gmail.com");

        //then
        Assertions.assertEquals(0, foundByEmail.size());
        Assertions.assertEquals(1, foundByEmail1.size());
        Assertions.assertTrue(foundByEmail2.contains(userService.findOneById(user2Id)));
        Assertions.assertEquals(user3Id, foundByEmail3.get(0).getId());
    }

    @Test
    public void findByUserRole() throws Exception {
        //given
        Long user1 = userService.join("gggabikr1@gmail.com", "Jason Lee", "aabbccde", "ROLE_GENERAL", "7788097503");
        Long user2 = userService.join("gggabikr2@gmail.com", "Justin Lee", "aabbccdeeee", "ROLE_OWNER", "7780000750");
        Long user3 = userService.join("gggabikr3@gmail.com", "James Lee", "aabbccdeeee", "ROLE_GENERAL", "7780007508");
        Long user4 = userService.join("gggabikr4@gmail.com", "Jay Lee", "aabbcee", "ROLE_OWNER", "7788097503");
        Long user5 = userService.join("gggabikr5@gmail.com", "John Lee", "aa3bcee", "ROLE_TABLE", "7780007508");


        //when
        List<User> role_table = userService.findByUserRole("ROLE_TABLE");
        List<User> role_owner = userService.findByUserRole("ROLE_GENERAL");
        List<User> role_general = userService.findByUserRole("ROLE_GENERAL");

        //then
        Assertions.assertEquals(2, role_owner.size());
        Assertions.assertEquals(user5, role_table.get(0).getId());
        Assertions.assertEquals(2, role_general.size());
        Assertions.assertTrue(role_general.contains(userService.findOneById(user1)));
        Assertions.assertTrue(role_general.contains(userService.findOneById(user3)));

    }

    @Test
    public void findByPhoneNumber() throws Exception {
        //given
        Long user1 = userService.join("gggabikr1@gmail.com", "Jason Lee", "aabbccde", "ROLE_GENERAL", "7788097503");
        Long user2 = userService.join("gggabikr2@gmail.com", "Justin Lee", "aabbccdeeee", "ROLE_OWNER", "7780000750");
        Long user3 = userService.join("gggabikr3@gmail.com", "James Lee", "aabbccdeeee", "ROLE_GENERAL", "7780007508");


        //when


        Assertions.assertEquals(user1, userService.findByPhoneNumber("7788097503").get(0).getId());
        Assertions.assertEquals(user2, userService.findByPhoneNumber("7780000750").get(0).getId());
        Assertions.assertEquals(user3, userService.findByPhoneNumber("7780007508").get(0).getId());
        Assertions.assertEquals(0, userService.findByPhoneNumber("7780001111").size());
        Assertions.assertEquals(0, userService.findByPhoneNumber("12341111").size());
    }
}