package com.menuit.menuitreplica.domain;

public enum UserRole {
    ROLE_OWNER("ROLE_OWNER"), ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_GENERAL("ROLE_GENERAL"), ROLE_TABLE("ROLE_TABLE");

    private final String text;

    UserRole(final String text){
        this.text = text;
    }
}
