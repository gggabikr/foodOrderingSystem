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

        //in case any of below conditions are false, then the flag will be false
        checkingErrorOfTimeString(openingTime, closingTime, lastCallTime);

        return hoursRepository.createHour(store, day, parseTime(openingTime), parseTime(closingTime), parseTime(lastCallTime));
    }



    public void duplicateHourForAllDays(Hours hour){
        hoursRepository.duplicateHourForAllDays(hour);
    }

    public void duplicateHourForSelectedDays(Hours hour, String... dayNames) {
        List<DayOfWeek> days = new ArrayList<>();
        for(String dayName: dayNames){
            days.add(DayOfWeek.valueOf(dayName.toUpperCase()));
        }
        DayOfWeek[] daysArray = days.toArray(new DayOfWeek[0]);
        hoursRepository.duplicateHourForSelectedDays(hour, daysArray);

    }

    public void setOpenHoursForAllDays(Long storeId ,String openingTime, String closingTime, String lastCallTime){
        Store store = storeRepository.findOne(storeId);


        //in case any of below conditions are false, then the flag will be false
        checkingErrorOfTimeString(openingTime, closingTime, lastCallTime);

        hoursRepository.setOpenHoursForAllDays(store, parseTime(openingTime), parseTime(closingTime), parseTime(lastCallTime));
    }

    private void checkingErrorOfTimeString(String openingTime, String closingTime, String lastCallTime) {
        boolean timeFlag = openingTime.length() == 5 && closingTime.length() == 5 && lastCallTime.length() == 5 &&
                openingTime.charAt(2) == ':' && closingTime.charAt(2) == ':' && lastCallTime.charAt(2) == ':';

        if (!timeFlag){
            throw new IllegalArgumentException("one or more of the time values are in an invalid format.");
        }
    }

    private LocalTime parseTime(String timeString) {
        String[] splitTime = timeString.split(":");
        return LocalTime.of(Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
    }


    public List<Hours> findByStore(Long storeId){
        Store store = storeRepository.findOne(storeId);

        return hoursRepository.findByStore(store);
    }
}
