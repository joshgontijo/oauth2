/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.AccessToken;
import com.josue.oauth.provider.entity.Authorization;
import com.josue.oauth.provider.entity.OAuthParam;
import com.josue.oauth.provider.ex.BadVerificationCodeException;
import com.josue.oauth.provider.ex.OAuthException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Josue
 */
@WebServlet(name = "TokenGrantServlet", urlPatterns = {"/oauth/token"})
public class TokenGrantServlet extends HttpServlet {

    @Inject
    OAuthControl control;

    private static final Logger logger = Logger.getLogger(TokenGrantServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject jsonObject = Json.createReader(request.getInputStream()).readObject();

        String code = jsonObject.getString(OAuthParam.CODE);
        String clientId = jsonObject.getString(OAuthParam.CLIENT_ID);
        String clientSecret = jsonObject.getString(OAuthParam.CLIENT_SECRET);

        logger.log(Level.INFO, "RECEIVED TOKEN REQUEST FOR CODE {0}", code);

        //Validate the request
        Authorization foundAuthProcess;
        try {
            foundAuthProcess = control.validateAuthorization(code, clientId, clientSecret);
        } catch (BadVerificationCodeException ex) {
            sendErrorResponse(response, ex);
            return;
        }

        AccessToken createdAccessToken = control.createAccessToken(foundAuthProcess);

        JsonObject responseJson = Json.createObjectBuilder().add(OAuthParam.ACCESS_TOKEN, createdAccessToken.getToken())
                .add(OAuthParam.REFRESH_TOKEN, createdAccessToken.getRefreshToken())
                .add(OAuthParam.EXPIRES_IN, createdAccessToken.getExpiresIn()).build();

        response.setContentType("application/json");
        response.getWriter().print(responseJson);

    }

    private void sendErrorResponse(HttpServletResponse response, OAuthException exception) throws IOException {
        JsonObject responseJson = Json.createObjectBuilder().add("error", exception.getError())
                .add("error_description", exception.getMessage()).build();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.getWriter().print(responseJson);
    }
}
