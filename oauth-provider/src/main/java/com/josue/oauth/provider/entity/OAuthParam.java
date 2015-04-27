/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider.entity;

/**
 *
 * @author Josue
 */
public class OAuthParam {

    //the client id
    public static final String CLIENT_ID = "client_id";
    //redirect should always match the registered utl
    public static final String REDIRECT_URL = "redirect_uri";
    //required scope by the client application
    public static final String SCOPE = "scope";
    //An unguessable random string sent by the client. It is used to protect against cross-site request forgery attacks.
    public static final String STATE = "scope";

}
