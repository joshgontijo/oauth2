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
public class AppSuspendedException extends OAuthException {

    public AppSuspendedException() {
        super("application_suspended", "Your application has been suspended");
    }

}
