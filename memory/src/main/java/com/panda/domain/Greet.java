package com.panda.domain;

/**
 * @author panda.
 * @since 2017-07-16 14:24.
 */
public class Greet {
    private long id;

    private String content;

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Greet(long id, String content) {
        this.id = id;
        this.content = content;
    }
}
