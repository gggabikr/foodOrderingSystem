package com.menuit.menuitreplica.domain;

import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String province;
    private String city;
    private String street;
    private String unit;
    private String zipcode;

    //this prevents using default constructor
    protected Address() {
    }

    public Address(String province ,String city, String street, String zipcode) {
        this.province = province;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public Address(String province ,String city, String street, String unit, String zipcode) {
        this.province = province;
        this.city = city;
        this.street = street;
        this.unit = unit;
        this.zipcode = zipcode;
    }
}