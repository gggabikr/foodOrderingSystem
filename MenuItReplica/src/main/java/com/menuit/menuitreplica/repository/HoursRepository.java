package com.menuit.menuitreplica.repository;

import com.menuit.menuitreplica.domain.Hours;
import com.menuit.menuitreplica.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HoursRepository {

    private final EntityManager em;

    public Hours findOne(Long hoursId){
        return em.find(Hours.class, hoursId);
    }

    public List<Hours> findAll(){
        return em.createQuery("select h from Hours h", Hours.class).getResultList();
    }

    public List<Hours> findOneByStoreAndDay(Store store, DayOfWeek day){
        return em.createQuery("select h from Hours h where h.store = :store and h.dayOfWeek = :day", Hours.class)
                .setParameter("store", store)
                .setParameter("day", day)
                .getResultList();
    }


    public Long createHour(Store store, DayOfWeek day, LocalTime openingTime, LocalTime closingTime, LocalTime lastCallTime){
        Hours hour = new Hours(day, openingTime, closingTime, lastCallTime);
        hour.setStore(store);

        List<Hours> byStoreAndDay = findOneByStoreAndDay(store, day);
        if(byStoreAndDay.size() > 0){
            for(Hours hours: byStoreAndDay){
                store.getOpenHours().remove(hours);
                em.remove(hours);
            }
        }
        em.persist(hour);
        store.getOpenHours().add(hour);
        return hour.getId();
    }

    public void duplicateHourForAllDays(Hours hour){
        List<Hours> hoursList = hour.duplicateHour(hour);
        for(Hours hours: hoursList){
            em.persist(hours);
        }
    }

    public void duplicateHourForSelectedDays(Hours hour, DayOfWeek... dayOfWeeks){
        List<Hours> hoursList = hour.duplicateHourForSelectedDays(hour, dayOfWeeks);
        for(Hours hours: hoursList){
            em.persist(hours);
        }
    }

    public List<Hours> findByStore(Store store){
        return em.createQuery("select h from Hours h where h.store = :store", Hours.class)
                .setParameter("store", store)
                .getResultList();
    }

}
