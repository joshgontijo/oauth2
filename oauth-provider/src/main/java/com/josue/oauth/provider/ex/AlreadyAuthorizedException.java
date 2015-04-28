/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider.ex;

/**
 *
 * @author Josue
 */
public class AlreadyAuthorizedException extends OAuthException {

    public AlreadyAuthorizedException() {
        super("already_authorized", "Your application already have permission to access this user data");
    }
}
