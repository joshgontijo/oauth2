/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.client;

import com.josue.oauth.client.entity.OAuthProvider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Josue
 */
@WebServlet(name = "RedirectServlet", urlPatterns = {"/redirect"})
public class RedirectServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(RedirectServlet.class.getName());

    //TODO check oauth names
    private static final String TEMP_CODE_PARAM = "code";

    @PersistenceContext
    EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = (String) request.getParameter(TEMP_CODE_PARAM);

        if (code != null) {
            LOG.log(Level.INFO, "RECEIVE CODE: {0}", code);
            requestToken(code);

            //redirect to the success page
            request.getRequestDispatcher("success.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private void requestToken(String code) {

        OAuthProvider oauthProvider = em.find(OAuthProvider.class, 1);//default, we have just one anyway

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("client_id", oauthProvider.getClientId())
                .add("client_secret", oauthProvider.getClientSecret())
                .add("code", code)
                .add("redirect_uri", oauthProvider.getRedirectUrl()).build();

        JsonObject responseJson = ClientBuilder.newClient().target("http://localhost:8080/oauth-provider")
                .path("token").request().post(Entity.entity(jsonObject, MediaType.APPLICATION_JSON), JsonObject.class);

        String accessToken = responseJson.get("access_token").toString();
        LOG.log(Level.INFO, "ACCESS TOKEN SUCESSFUL RECEVIED !!!: {0}", accessToken);

//        target.request(MediaType.APPLICATION_JSON).post(null)
    }

}
