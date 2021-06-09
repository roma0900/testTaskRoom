package com.example.testTaskRoomservice;

import javax.persistence.*;

//class for logging all about user actions
@Entity
public class UserAction {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer  id;

    private Integer roomId;
    private boolean entrance;
    private Integer keyId;
    private String response;

    public UserAction(Integer roomId, Integer keyId, boolean entrance) {
        this.roomId = roomId;
        this.keyId = keyId;
        this.entrance = entrance;
    }
    public UserAction(){}

    public Integer getId() {
        return id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public boolean getEntrance() {
        return entrance;
    }

    public void setEntrance(boolean entrance) {
        this.entrance = entrance;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
