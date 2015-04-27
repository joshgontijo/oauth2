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
public class RequiredParamException extends OAuthException {

    public RequiredParamException(String paramName) {
        super("missing_param", "Missing param: '" + paramName + "'");
    }

}
