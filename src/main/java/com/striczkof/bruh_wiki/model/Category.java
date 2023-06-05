package com.striczkof.bruh_wiki.model;

import java.io.Serializable;

public class Category implements Serializable {
    private int id; // Unique ID. INT type.
    private String name; // Category name. TINYTEXT type.

    public Category() {
        // Java Bean constructor
    }

    // All variables initialized constructor
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
