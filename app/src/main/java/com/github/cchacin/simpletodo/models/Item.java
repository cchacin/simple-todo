package com.github.cchacin.simpletodo.models;

import com.orm.SugarRecord;

public class Item extends SugarRecord<Item> {
    private final String text;

    public Item(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
