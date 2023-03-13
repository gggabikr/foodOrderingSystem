package com.menuit.menuitreplica.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
public class Hours {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hours_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    //if I set the name to 'day' it will make an error on DB
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    //Monday, Tuesday, Wednesday, Thursday,
    //Friday, Saturday, Sunday

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "last_call")
    private LocalTime lastCallTime;

    protected Hours(){};

    public Hours(DayOfWeek day, LocalTime opening, LocalTime closing, LocalTime lastCall){
    //check if opening is before both closing and lastCall, and if closing is after lastCall

        boolean flag1 = (lastCall.isBefore(closing) || lastCall.equals(closing) || closing.equals(LocalTime.MIDNIGHT));
        boolean flag2 = ((closing.isAfter(opening) || closing.equals(opening)) || closing.equals(LocalTime.MIDNIGHT) && opening.isBefore(closing));
        boolean flag3 = (lastCall.isAfter(opening) || lastCall.equals(opening) || lastCall.equals(closing));

        if (!flag1 && !flag2 && !flag3){
            throw new IllegalArgumentException("Opening time needs to be earliest, " +
                    "closing time needs to be latest " +
                    "and last call time needs to be between the two.");
        }
        this.dayOfWeek = day;
        this.openingTime = opening;
        this.closingTime = closing;
        this.lastCallTime = lastCall;
    }

    public void setStore(Store store){
        this.store = store;
    }

    public List<Hours> duplicateHour(Hours hour) {
        // Create new Hours objects for all other days of the week
        Store store = hour.getStore();

        List<Hours> hoursList = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day != this.dayOfWeek) {
                Hours newHours = new Hours(day, this.openingTime, this.closingTime, this.lastCallTime);
                newHours.setStore(store);
                hoursList.add(newHours);
            }
        }

        // Remove existing Hours objects with the same dayOfWeek value
        List<Hours> existingHours = store.getOpenHours().stream()
                .filter(h -> h.getDayOfWeek() != this.dayOfWeek)
                .collect(Collectors.toList());
        store.getOpenHours().removeAll(existingHours);

        // Add the new Hours objects to the store's list of hours
        store.getOpenHours().addAll(hoursList);

        return hoursList;
    }

    public List<Hours> duplicateHourForSelectedDays(Hours hour, DayOfWeek... dayOfWeeks) {
        Store store = hour.getStore();
        List<Hours> hoursList = new ArrayList<>();

        for (DayOfWeek day : dayOfWeeks) {
            List<Hours> existingHours = store.getOpenHours().stream()
                    .filter(h -> h.getDayOfWeek() == day)
                    .collect(Collectors.toList());
            store.getOpenHours().removeAll(existingHours);
            Hours newHours = new Hours(day, hour.getOpeningTime(), hour.getClosingTime(), hour.getLastCallTime());
            newHours.setStore(store);
            store.getOpenHours().add(newHours);
            hoursList.add(newHours);
        }
        return hoursList;
    }

}
