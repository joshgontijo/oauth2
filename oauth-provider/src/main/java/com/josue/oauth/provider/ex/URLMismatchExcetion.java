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
public class URLMismatchExcetion extends OAuthException {

    public URLMismatchExcetion() {
        super("redirect_uri_mismatch", "The redirect_uri MUST match the registered callback URL for this application.");
    }

}
