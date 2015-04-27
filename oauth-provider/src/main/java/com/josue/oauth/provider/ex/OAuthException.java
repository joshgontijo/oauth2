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
public class OAuthException extends Exception {
//    BAD_VERIFICATION_CODE("")

    private final String error;

    public OAuthException(String error, String message) {
        super(message);
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
