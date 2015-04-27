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
public class BadVerificationCodeException extends OAuthException {

    public BadVerificationCodeException() {
        super("bad_verification_code", "The code passed is incorrect or expired.");
    }
}
