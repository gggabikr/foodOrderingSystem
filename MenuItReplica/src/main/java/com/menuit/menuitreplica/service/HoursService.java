package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.Hours;
import com.menuit.menuitreplica.domain.Store;
import com.menuit.menuitreplica.repository.HoursRepository;
import com.menuit.menuitreplica.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HoursService {

    private final HoursRepository hoursRepository;
    private final StoreRepository storeRepository;

    public Hours findOne(Long hoursId){
        return hoursRepository.findOne(hoursId);
    }

    public List<Hours> findAll(){
        return hoursRepository.findAll();
    }

    public List<Hours> findOneByStoreAndDay(Long storeId, String dayName){
        Store store = storeRepository.findOne(storeId);
        DayOfWeek day = DayOfWeek.valueOf(dayName);
        return hoursRepository.findOneByStoreAndDay(store, day);
    }

    public Long createHour(Long storeId, String dayName, String openingTime, String closingTime, String lastCallTime){
        Store store = storeRepository.findOne(storeId);
        DayOfWeek day = DayOfWeek.valueOf(dayName);

        String[] splitOpening = openingTime.split(":");
        LocalTime openingLocalTime = LocalTime.of(Integer.parseInt(splitOpening[0]), Integer.parseInt(splitOpening[1]));

        String[] splitClosing = closingTime.split(":");
        LocalTime closingLocalTime = LocalTime.of(Integer.parseInt(splitClosing[0]), Integer.parseInt(splitClosing[1]));

        String[] splitLastCall = lastCallTime.split(":");
        LocalTime lastCallLocalTime = LocalTime.of(Integer.parseInt(splitLastCall[0]), Integer.parseInt(splitLastCall[1]));

        return hoursRepository.createHour(store, day, openingLocalTime,closingLocalTime,lastCallLocalTime);
    }

    public void duplicateHourForAllDays(Hours hour){
        hoursRepository.duplicateHourForAllDays(hour);
    }

    public void duplicateHourForSelectedDays(Hours hour, String... dayNames) {
        List<DayOfWeek> days = new ArrayList<>();
        for(String dayName: dayNames){
            days.add(DayOfWeek.valueOf(dayName.toUpperCase()));
        }
        DayOfWeek[] daysArray = days.toArray(new DayOfWeek[days.size()]);
        hoursRepository.duplicateHourForSelectedDays(hour, daysArray);

    }

    public void setOpenHoursForAllDays(Long storeId ,String openingTime, String closingTime, String lastCallTime){
        Store store = storeRepository.findOne(storeId);

        String[] splitOpening = openingTime.split(":");
        LocalTime openingLocalTime = LocalTime.of(Integer.parseInt(splitOpening[0]), Integer.parseInt(splitOpening[1]));

        String[] splitClosing = closingTime.split(":");
        LocalTime closingLocalTime = LocalTime.of(Integer.parseInt(splitClosing[0]), Integer.parseInt(splitClosing[1]));

        String[] splitLastCall = lastCallTime.split(":");
        LocalTime lastCallLocalTime = LocalTime.of(Integer.parseInt(splitLastCall[0]), Integer.parseInt(splitLastCall[1]));

        hoursRepository.setOpenHoursForAllDays(store, openingLocalTime,closingLocalTime,lastCallLocalTime);
    }

    public List<Hours> findByStore(Long storeId){
        Store store = storeRepository.findOne(storeId);

        return hoursRepository.findByStore(store);
    }
}
