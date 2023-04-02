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

        //TODO: 컨디션 세팅 다시하거나, 아예 하지말자.
        //1. 오전에 열어서 오후에 닫음, 둘 사이에 라스트콜
        //2. 아무때나 열어서 다음날 새벽에 닫음. 둘 사이에 라스트콜
        //3. 아무때나 열어서 자정에 닫음. 라스트콜은 자정 전
        //4. 아무때나 열어서 다음날 새벽에 닫음. 라스트콜이 자정
        //5. 자정에 열어서 오전에 닫음. 라스트콜은 이른새벽
        //까지가 통과 되어야 하는 케이스들.
        //어떤 이유에서든 라스트콜이 둘 사이에 있지않거나,
        //혹은 오픈시간이 클로즈보다 4시간 미만으로 많을때는 오류있어야...
        //오픈 7시 클로즈 2시와 같이 단순히 보기엔 오픈이 클로즈시간보다 늦은시간처럼 보이는 스토어도 그런 스토어도 존재할수있다.
//
//        boolean flag = (lastCall.isAfter(opening) || lastCall.equals(opening) || lastCall.equals(closing))
//                && ((closing.isAfter(opening) || closing.equals(opening)) || closing.equals(LocalTime.MIDNIGHT) && opening.isBefore(closing))
//                && (lastCall.isBefore(closing) || lastCall.equals(closing) || closing.equals(LocalTime.MIDNIGHT));
//

//        boolean flag1 = (lastCall.isBefore(closing) || lastCall.equals(closing) || closing.equals(LocalTime.MIDNIGHT));
//        boolean flag2 = ((closing.isAfter(opening) || closing.equals(opening)) || closing.equals(LocalTime.MIDNIGHT) && opening.isBefore(closing));
//        boolean flag3 = (lastCall.isAfter(opening) || lastCall.equals(opening) || lastCall.equals(closing));

//        if (!flag1 || !flag2 || !flag3){
//        if(!flag){
//            throw new IllegalArgumentException("Opening time needs to be earliest, " +
//                    "closing time needs to be latest " +
//                    "and last call time needs to be between the two.");
//        }
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
            for (Hours hour2: existingHours){
                hour2.setStore(null);
            }

            Hours newHours = new Hours(day, hour.getOpeningTime(), hour.getClosingTime(), hour.getLastCallTime());
            newHours.setStore(store);
            hoursList.add(newHours);
        }
        store.getOpenHours().addAll(hoursList);
        return hoursList;
    }

}
