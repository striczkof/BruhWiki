package striczkof.bruh_wiki.model;

import java.io.Serializable;

public class Article implements Serializable {
    private int id; // Unique ID. INT type.
    private int categoryId; // Unique Category ID. INT type.
    private long made; // Unix Timestamp. BIGINT type.
    private long lastEdited; // Unix timestamp. BIGINT type.
    private String title; // Article title. TINYTEXT type.
    private String content; // Article content. LONGTEXT type.

    private boolean hidden; // Is article hidden? BOOLEAN type.

    public Article() {
        // Java Bean constructor
    }

    // All variables initialized constructor
    public Article(int id, int categoryId, long made, long lastEdited, String title, String content, boolean hidden) {
        this.id = id;
        this.categoryId = categoryId;
        this.made = made;
        this.lastEdited = lastEdited;
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
