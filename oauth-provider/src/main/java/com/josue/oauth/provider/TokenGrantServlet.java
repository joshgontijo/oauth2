/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.Authorization;
import java.io.IOException;
import java.util.UUID;
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
@WebServlet(name = "TokenGrantServlet", urlPatterns = {"/token"})
public class TokenGrantServlet extends HttpServlet {

    @Inject
    OAuthControl control;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JsonObject jsonObject = Json.createReader(request.getInputStream()).readObject();

            String code = jsonObject.getString("code");
            LOG.log(Level.INFO, "RECEIVED TOKEN REQUEST FOR CODE {0}", code);

            Authorization foundAuthProcess = control.fetchAuthorization(code);
            if (foundAuthProcess == null) {
                throw new Exception("Code not found");
            }

            String accessToken = String.valueOf(UUID.randomUUID().getMostSignificantBits());
            String refreshToken = String.valueOf(UUID.randomUUID().getMostSignificantBits());
            int expiresIn = 86400;

            JsonObject responseJson = Json.createObjectBuilder().add("access_token", accessToken)
                    .add("refresh_token", refreshToken)
                    .add("expires_in", expiresIn).build();

            response.setContentType("application/json");
            response.getWriter().print(responseJson);

        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }

    }
    private static final Logger LOG = Logger.getLogger(TokenGrantServlet.class.getName());

}
