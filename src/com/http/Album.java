package com.http;

public class Album {
    private int id;
    private int userId;
    private String title;

    public Album(int id, int userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                '}';
    }
}
