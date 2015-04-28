/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Josue
 */
@Entity
@Table(name = "oauth_authorization")
public class Authorization {

    public static enum Status {

        ACTIVE,
        APPLIED, //Sucessfully utilized
        INACTIVE //expired or invalidated
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    //Time to use this code, 10 minutes max is recommnded
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires", columnDefinition = "TIMESTAMP")
    private Date expires;

    @OneToOne
    private Application application;

    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Authorization() {
    }

    public Authorization(String code, Date expires, Application application, User user, Status status) {
        this.code = code;
        this.expires = expires;
        this.application = application;
        this.user = user;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
