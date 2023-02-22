package com.menuit.menuitreplica.domain;

public enum TaxInfo {

    NoTax("NOTAX"), GST("GST"), PST("PST"), BOTH("BOTH");

    private final String text;

    TaxInfo(final String text){
        this.text = text;
    }
}
