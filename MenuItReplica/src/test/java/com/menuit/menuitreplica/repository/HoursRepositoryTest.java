package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HoursRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    HoursRepository hoursRepository;


    @Test
    public void findOne() throws Exception {

        //given
        User ownerUser1 = new User();
        ownerUser1.setEmail("gggabikr3@gmail.com");
        ownerUser1.setFullName("Jason Kim");
        ownerUser1.setPassword("aabbccde");
        ownerUser1.setPhone("7788097503");
        ownerUser1.setRole(UserRole.ROLE_OWNER);


        User ownerUser2 = new User();
        ownerUser2.setEmail("gggab4@gmail.com");
        ownerUser2.setFullName("Jacob Lee");
        ownerUser2.setPassword("abcdef");
        ownerUser2.setPhone("7787503982");
        ownerUser2.setRole(UserRole.ROLE_OWNER);


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

        LocalTime localTime0930 = LocalTime.of(9,30);
        LocalTime localTime2300 = LocalTime.of(23,0);
        LocalTime localTime2400= LocalTime.MIDNIGHT;

        Long MonHourId = hoursRepository.createHour(store1, DayOfWeek.valueOf("MONDAY"), localTime0930, localTime2400, localTime2300);
        Hours MonHour = hoursRepository.findOne(MonHourId);
        hoursRepository.duplicateHourForAllDays(MonHour);

        Assertions.assertEquals(localTime0930, store1.getHoursForGivenDay(DayOfWeek.valueOf("MONDAY")).getOpeningTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("MONDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("MONDAY")).getClosingTime());
        Assertions.assertEquals(localTime0930, store1.getHoursForGivenDay(DayOfWeek.valueOf("TUESDAY")).getOpeningTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("TUESDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("TUESDAY")).getClosingTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("WEDNESDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("WEDNESDAY")).getClosingTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("WEDNESDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("THURSDAY")).getClosingTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("THURSDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("THURSDAY")).getClosingTime());
        Assertions.assertEquals(localTime0930, store1.getHoursForGivenDay(DayOfWeek.valueOf("FRIDAY")).getOpeningTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("FRIDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("FRIDAY")).getClosingTime());
        Assertions.assertEquals(localTime0930, store1.getHoursForGivenDay(DayOfWeek.valueOf("SATURDAY")).getOpeningTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("SATURDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("SATURDAY")).getClosingTime());
        Assertions.assertEquals(localTime0930, store1.getHoursForGivenDay(DayOfWeek.valueOf("SUNDAY")).getOpeningTime());
        Assertions.assertEquals(localTime2300, store1.getHoursForGivenDay(DayOfWeek.valueOf("SUNDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2400, store1.getHoursForGivenDay(DayOfWeek.valueOf("SUNDAY")).getClosingTime());

        LocalTime localTime1130 = LocalTime.of(11,30);
        LocalTime localTime2000 = LocalTime.of(20,0);
        LocalTime localTime1900= LocalTime.of(19,0);

        Long weekendHourId = hoursRepository.createHour(store1, DayOfWeek.valueOf("SATURDAY"), localTime1130, localTime2000, localTime1900);
        Hours weekendHour = hoursRepository.findOne(weekendHourId);
        hoursRepository.duplicateHourForSelectedDays(weekendHour,DayOfWeek.valueOf("SUNDAY"));

        Assertions.assertEquals(localTime1130, store1.getHoursForGivenDay(DayOfWeek.valueOf("SATURDAY")).getOpeningTime());
        Assertions.assertEquals(localTime1900, store1.getHoursForGivenDay(DayOfWeek.valueOf("SATURDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2000, store1.getHoursForGivenDay(DayOfWeek.valueOf("SATURDAY")).getClosingTime());
        Assertions.assertEquals(localTime1130, store1.getHoursForGivenDay(DayOfWeek.valueOf("SUNDAY")).getOpeningTime());
        Assertions.assertEquals(localTime1900, store1.getHoursForGivenDay(DayOfWeek.valueOf("SUNDAY")).getLastCallTime());
        Assertions.assertEquals(localTime2000, store1.getHoursForGivenDay(DayOfWeek.valueOf("SUNDAY")).getClosingTime());

        store1.printOpenHours();
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findOneByStoreAndDay() {
    }

    @Test
    public void createHour() {
    }

    @Test
    public void duplicateHourForAllDays() {
    }

    @Test
    public void duplicateHourForSelectedDays() {
    }

    @Test
    public void findByStore() {
    }
}