/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.client.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Josue
 */
@Entity
@Table(name = "oauth_provider")
public class OAuthProvider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "provider_name")
    private String oauthProviderName;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "redirect_url")
    private String redirectUrl;

    public OAuthProvider() {
    }

    public OAuthProvider(Integer id, String oauthProviderName, String clientId, String clientSecret, String redirectUrl) {
        this.id = id;
        this.oauthProviderName = oauthProviderName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOauthProviderName() {
        return oauthProviderName;
    }

    public void setOauthProviderName(String oauthProviderName) {
        this.oauthProviderName = oauthProviderName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
