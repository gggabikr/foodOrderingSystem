package com.menuit.menuitreplica.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.time.DayOfWeek;

@Entity
@Getter @Setter
public class Hours {

    @Id @GeneratedValue
    @Column(name="hours_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    //if I set the name to 'day' it will make an error on DB
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    //Monday, Tuesday, Wednesday, Thursday,
    //Friday, Saturday, Sunday

    private Time opening;

    private Time closing;

    protected Hours(){};

    public Hours(DayOfWeek day, Time opening, Time closing){
        this.dayOfWeek = day;
        this.opening = opening;
        this.closing = closing;
    }
}
