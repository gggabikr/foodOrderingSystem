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
    public void multiTest() throws Exception {

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

        for(Hours hour: hoursRepository.findAll()){
            System.out.println(hour.getStore().getName());
            System.out.println(hour.getId());
            System.out.println(hour.getDayOfWeek().name());
            System.out.println(hour.getOpeningTime());
            System.out.println(hour.getClosingTime());
            System.out.println(hour.getLastCallTime());
            System.out.println("---------------------");
        }
        Assertions.assertEquals(7, hoursRepository.findAll().size());


        LocalTime localTime1800 = LocalTime.of(18,0);
        LocalTime localTime0300 = LocalTime.of(3,0);
        LocalTime localTime0230 = LocalTime.of(2,30);

        hoursRepository.setOpenHoursForAllDays(store2, localTime1800, localTime0300, localTime0230);
        for(Hours hour: store2.getOpenHours()){
            Assertions.assertEquals(localTime1800, hour.getOpeningTime());
            Assertions.assertEquals(localTime0300, hour.getClosingTime());
            Assertions.assertEquals(localTime0230, hour.getLastCallTime());
        }
        for(Hours hour: hoursRepository.findByStore(store2)){
            Assertions.assertEquals(localTime1800, hour.getOpeningTime());
            Assertions.assertEquals(localTime0300, hour.getClosingTime());
            Assertions.assertEquals(localTime0230, hour.getLastCallTime());
        }
        Assertions.assertEquals(14, hoursRepository.findAll().size());

        Assertions.assertEquals(localTime0930, hoursRepository.findOneByStoreAndDay(store1, DayOfWeek.FRIDAY).get(0).getOpeningTime());
        Assertions.assertEquals(localTime2400, hoursRepository.findOneByStoreAndDay(store1, DayOfWeek.FRIDAY).get(0).getClosingTime());
        Assertions.assertEquals(localTime2300, hoursRepository.findOneByStoreAndDay(store1, DayOfWeek.FRIDAY).get(0).getLastCallTime());

        Assertions.assertEquals(localTime1130, hoursRepository.findOneByStoreAndDay(store1, DayOfWeek.SATURDAY).get(0).getOpeningTime());
        Assertions.assertEquals(localTime2000, hoursRepository.findOneByStoreAndDay(store1, DayOfWeek.SATURDAY).get(0).getClosingTime());
        Assertions.assertEquals(localTime1900, hoursRepository.findOneByStoreAndDay(store1, DayOfWeek.SATURDAY).get(0).getLastCallTime());

        Long iddd = hoursRepository.createHour(store2, DayOfWeek.MONDAY, localTime1130, localTime2000, localTime1900);
        Hours hour = hoursRepository.findOne(iddd);
        hoursRepository.duplicateHourForSelectedDays(hour, DayOfWeek.TUESDAY);

        Assertions.assertEquals(7, store2.getOpenHours().size());
        Assertions.assertEquals(localTime1130, store2.getHoursForGivenDay(DayOfWeek.MONDAY).getOpeningTime());
        Assertions.assertEquals(localTime2000, store2.getHoursForGivenDay(DayOfWeek.MONDAY).getClosingTime());
        Assertions.assertEquals(localTime1900, store2.getHoursForGivenDay(DayOfWeek.MONDAY).getLastCallTime());

        Assertions.assertEquals(localTime1130, store2.getHoursForGivenDay(DayOfWeek.TUESDAY).getOpeningTime());
        Assertions.assertEquals(localTime2000, store2.getHoursForGivenDay(DayOfWeek.TUESDAY).getClosingTime());
        Assertions.assertEquals(localTime1900, store2.getHoursForGivenDay(DayOfWeek.TUESDAY).getLastCallTime());

        Assertions.assertEquals(localTime1800, store2.getHoursForGivenDay(DayOfWeek.WEDNESDAY).getOpeningTime());
        Assertions.assertEquals(localTime0300, store2.getHoursForGivenDay(DayOfWeek.WEDNESDAY).getClosingTime());
        Assertions.assertEquals(localTime0230, store2.getHoursForGivenDay(DayOfWeek.WEDNESDAY).getLastCallTime());


        System.out.println("---------------");
        System.out.println(hoursRepository.findByStore(store2).size());
        for(Hours hours: store2.getOpenHours()){
            System.out.println(hours.getDayOfWeek().name());
            System.out.println(hours.getOpeningTime());
            System.out.println(hours.getClosingTime());
            System.out.println(hours.getLastCallTime());
        }
        System.out.println("---------------");

        hoursRepository.duplicateHourForAllDays(hour);

        Assertions.assertEquals(7, store2.getOpenHours().size());
        for(Hours hours: store2.getOpenHours()){
            Assertions.assertEquals(localTime1130, hours.getOpeningTime());
            Assertions.assertEquals(localTime2000, hours.getClosingTime());
            Assertions.assertEquals(localTime1900, hours.getLastCallTime());
        }

        System.out.println("Open hours size: " + store2.getOpenHours().size());
        System.out.println("findByStore size: " + hoursRepository.findByStore(store2).size());
        Assertions.assertArrayEquals(store2.getOpenHours().toArray(), hoursRepository.findByStore(store2).toArray());
        Assertions.assertArrayEquals(store1.getOpenHours().toArray(), hoursRepository.findByStore(store1).toArray());


//        Hours hour1 = new Hours(DayOfWeek.MONDAY, LocalTime.of(7, 0), LocalTime.of(15, 0), LocalTime.of(14, 30));
//        Hours hour2 = new Hours(DayOfWeek.TUESDAY, LocalTime.of(9, 30), LocalTime.of(18, 0), LocalTime.of(17, 30));
//        Hours hour3 = new Hours(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0), LocalTime.of(22, 0), LocalTime.of(21, 0));
//        Hours hour4 = new Hours(DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(23, 30), LocalTime.of(23, 0));
//        Hours hour5 = new Hours(DayOfWeek.FRIDAY, LocalTime.of(16, 30), LocalTime.of(1, 0), LocalTime.of(0, 30));
//        Hours hour6 = new Hours(DayOfWeek.SATURDAY, LocalTime.of(18, 0), LocalTime.of(3, 0), LocalTime.of(2, 0));
//        Hours hour7 = new Hours(DayOfWeek.SUNDAY, LocalTime.of(20, 0), LocalTime.of(5, 0), LocalTime.of(4, 0));
//        Hours hour8 = new Hours(DayOfWeek.MONDAY, LocalTime.of(0, 0), LocalTime.of(12, 0), LocalTime.of(11, 30));
//        Hours hour9 = new Hours(DayOfWeek.TUESDAY, LocalTime.of(12, 0), LocalTime.of(23, 59, 59), LocalTime.of(23, 30));
//        Hours hour10 = new Hours(DayOfWeek.WEDNESDAY, LocalTime.of(22, 0), LocalTime.of(2, 0), LocalTime.of(1, 0));
//        Hours hour11 = new Hours(DayOfWeek.THURSDAY, LocalTime.of(3, 0), LocalTime.of(9, 0), LocalTime.of(8, 30));
//        Hours hour12 = new Hours(DayOfWeek.FRIDAY, LocalTime.of(5, 30), LocalTime.of(14, 0), LocalTime.of(13, 30));
//        Hours hour13 = new Hours(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(18, 30), LocalTime.of(18, 0));
//        Hours hour14 = new Hours(DayOfWeek.SUNDAY, LocalTime.of(19, 0), LocalTime.of(3, 30), LocalTime.of(3, 0));
//        Hours hour15 = new Hours(DayOfWeek.MONDAY, LocalTime.of(23, 0), LocalTime.of(4, 0), LocalTime.of(3, 0));
//        Hours hour16 = new Hours(DayOfWeek.TUESDAY, LocalTime.of(5, 0), LocalTime.of(10, 0), LocalTime.of(9, 30));
//        Hours hour17 = new Hours(DayOfWeek.WEDNESDAY, LocalTime.of(12, 0), LocalTime.of(20, 0), LocalTime.of(19, 0));
//        Hours hour18 = new Hours(DayOfWeek.THURSDAY, LocalTime.of(17, 30), LocalTime.of(23, 0), LocalTime.of(22, 30));
//        Hours hour19 = new Hours(DayOfWeek.FRIDAY, LocalTime.of(19, 0), LocalTime.of(2, 0),LocalTime.of(0, 0));
//        Hours hour20 = new Hours(DayOfWeek.FRIDAY, LocalTime.of(0, 0), LocalTime.of(3, 0),LocalTime.of(1, 0));
//        Hours hour21 = new Hours(DayOfWeek.FRIDAY, LocalTime.of(23, 0), LocalTime.of(2, 0),LocalTime.of(0, 0));
//        Hours hour22 = new Hours(DayOfWeek.FRIDAY, LocalTime.of(17, 0), LocalTime.of(23, 0),LocalTime.of(0, 0));
//        Hours hour23 = new Hours(DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(11, 0), LocalTime.of(10, 0));
//        Hours hour24 = new Hours(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(9, 0), LocalTime.of(11, 0));
//        Hours hour25 = new Hours(DayOfWeek.WEDNESDAY, LocalTime.of(0, 0), LocalTime.of(0, 0), LocalTime.of(0, 0));
//        Hours hour26 = new Hours(DayOfWeek.THURSDAY, LocalTime.of(22, 0), LocalTime.of(8, 0), LocalTime.of(23, 0));
//        Hours hour27 = new Hours(DayOfWeek.FRIDAY, LocalTime.of(23, 0), LocalTime.of(1, 0), LocalTime.of(22, 0));

    }
}