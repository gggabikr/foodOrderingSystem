package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.Address;
import com.menuit.menuitreplica.domain.Hours;
import com.menuit.menuitreplica.domain.Store;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HoursServiceTest {

    @Autowired
    StoreService storeService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    ItemService itemService;
    @Autowired
    HoursService hoursService;

    @Test
    public void multiTests() throws Exception {
        Long user1Id = userService.join("gggabike@gmail.com", "Jason Lee", "AaaBbbCcD12", "ROLE_OWNER", "7788097503");
        Long user2Id = userService.join("ssssssu984@gmail.com", "Breece Park", "ssaaddcc", "ROLE_OWNER", "9992223333");

        Address address1 = new Address("B.C", "Vancouver", "1990 W 41st Ave", "V6M 1Y4");
        Long store1Id = storeService.registerStore("Jason's kitchen", address1, "7788097503", user1Id);

        Address address2 = new Address("B.C", "Vancouver", "2581 W 32nd Ave", "V6M 0A4");
        Long store2Id = storeService.registerStore("Breece's Pizza", address2, "1232343456", user2Id);

        hoursService.setOpenHoursForAllDays(store1Id, "11:00", "22:00", "21:00");

        Store store1 = storeService.findOne(store1Id);
        Store store2 = storeService.findOne(store2Id);


        for(DayOfWeek day: DayOfWeek.values()){
            Assertions.assertEquals(LocalTime.of(11,0), store1.getHoursForGivenDay(day).getOpeningTime());
            Assertions.assertEquals(LocalTime.of(22,0), store1.getHoursForGivenDay(day).getClosingTime());
            Assertions.assertEquals(LocalTime.of(21,0), store1.getHoursForGivenDay(day).getLastCallTime());
        }

        hoursService.setOpenHoursForAllDays(store2Id, "07:00", "16:00", "15:30");

        hoursService.createHour(store2Id, "SATURDAY", "08:00", "15:00", "14:30");
        List<Hours> saturdayHour = hoursService.findOneByStoreAndDay(store2Id, "SATURDAY");
        hoursService.duplicateHourForSelectedDays(saturdayHour.get(0),"SUNDAY");

//        ArrayList<DayOfWeek> weekdays = new ArrayList<>();
//        for()
//        weekdays.add(DayOfWeek.MONDAY);

        for(int i = 1; i < 6; i++){
            Assertions.assertEquals(LocalTime.of(7,0), store2.getHoursForGivenDay(DayOfWeek.of(i)).getOpeningTime());
            Assertions.assertEquals(LocalTime.of(16,0), store2.getHoursForGivenDay(DayOfWeek.of(i)).getClosingTime());
            Assertions.assertEquals(LocalTime.of(15,30), store2.getHoursForGivenDay(DayOfWeek.of(i)).getLastCallTime());
        }

        for(int i = 6; i < 8; i++){
            Assertions.assertEquals(LocalTime.of(8,0), store2.getHoursForGivenDay(DayOfWeek.of(i)).getOpeningTime());
            Assertions.assertEquals(LocalTime.of(15,0), store2.getHoursForGivenDay(DayOfWeek.of(i)).getClosingTime());
            Assertions.assertEquals(LocalTime.of(14,30), store2.getHoursForGivenDay(DayOfWeek.of(i)).getLastCallTime());
        }

        Assertions.assertEquals(7, store1.getOpenHours().size());
        Assertions.assertEquals(7, store2.getOpenHours().size());
        Assertions.assertEquals(7, hoursService.findByStore(store1Id).size());
        Assertions.assertEquals(7, hoursService.findByStore(store2Id).size());
        Assertions.assertEquals(14, hoursService.findAll().size());

        hoursService.duplicateHourForAllDays(hoursService.findOneByStoreAndDay(store2Id,"SUNDAY").get(0));

        for(int i = 1; i < 8; i++){
            Assertions.assertEquals(LocalTime.of(8,0), store2.getHoursForGivenDay(DayOfWeek.of(i)).getOpeningTime());
            Assertions.assertEquals(LocalTime.of(15,0), store2.getHoursForGivenDay(DayOfWeek.of(i)).getClosingTime());
            Assertions.assertEquals(LocalTime.of(14,30), store2.getHoursForGivenDay(DayOfWeek.of(i)).getLastCallTime());
        }

        Assertions.assertEquals(7, store2.getOpenHours().size());
        Assertions.assertEquals(7, hoursService.findByStore(store2Id).size());
        Assertions.assertEquals(14, hoursService.findAll().size());

    }



}