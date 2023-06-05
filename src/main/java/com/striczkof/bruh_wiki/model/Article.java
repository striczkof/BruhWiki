package com.striczkof.bruh_wiki.model;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
    private int id; // Unique ID. INT type.
    private int categoryId; // Unique Category ID. INT type.
    private String categoryName; // Category name. TINYTEXT type, joined.
    private long made; // Unix Timestamp. BIGINT type.
    private long lastEdited; // Unix timestamp. BIGINT type.
    private Date madeDate; // Date object. not stored in database.
    private Date lastEditedDate; // Date object. not stored in database.
    private String title; // Article title. TINYTEXT type.
    private String content; // Article content. LONGTEXT type.

    private boolean hidden; // Is article hidden? BOOLEAN type.

    public Article() {
        // Java Bean constructor
    }

    // All variables initialized constructor
    public Article(int id, int categoryId, String categoryName, long made, long lastEdited, String title, String content, boolean hidden) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.made = made;
        this.lastEdited = lastEdited;
        this.madeDate = new Date(made * 1000);
        this.lastEditedDate = new Date(lastEdited * 1000);
        this.title = title;
        this.content = content;
        this.hidden = hidden;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getMade() {
        return made;
    }

    public void setMade(long made) {
        this.made = made;
    }

    public long getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(long lastEdited) {
        this.lastEdited = lastEdited;
    }

    public Date getMadeDate() {
        return madeDate;
    }

    public void setMadeDate(Date madeDate) {
        this.madeDate = madeDate;
    }

    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
