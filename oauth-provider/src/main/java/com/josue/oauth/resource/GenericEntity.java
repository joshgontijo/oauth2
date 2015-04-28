/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.resource;

import com.josue.oauth.provider.entity.User;

/**
 *
 * @author Josue
 */
public class GenericEntity {

    private int id;
    private User owner;
    private String message;

    public GenericEntity() {
    }

    public GenericEntity(int id, User owner, String message) {
        this.id = id;
        this.owner = owner;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
