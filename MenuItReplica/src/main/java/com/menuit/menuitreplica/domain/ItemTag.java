package com.menuit.menuitreplica.domain;

public enum ItemTag {
    Popular("POPULAR"), Best("BEST"), Recommended("RECOMMENDED"), NoTag("NOTAG");

    private final String text;

    ItemTag(final String text){
        this.text = text;
    }
}
