package com.menuit.menuitreplica.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_OWNER("ROLE_OWNER"),
    ROLE_GENERAL("ROLE_GENERAL"),
    ROLE_TABLE("ROLE_TABLE");


    UserRole(final String text){
        this.text = text;
    }

    private final String text;

}
