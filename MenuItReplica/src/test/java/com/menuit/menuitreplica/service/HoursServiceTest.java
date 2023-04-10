package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.repository.OrderRepository;
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
    public void findOne() {
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
    public void setOpenHoursForAllDays() {
    }

    @Test
    public void findByStore() {
    }
}