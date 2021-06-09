package com.example.testTaskRoomservice;

import javax.persistence.*;

//I made this class to remember if the user is currently in the room.
@Entity
public class User {
    @Id
    private Integer id;
    private Integer roomId;

    public User(Integer id, Integer roomId) {
        this.id = id;
        this.roomId = roomId;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
