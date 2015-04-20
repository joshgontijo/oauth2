/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.User;
import java.io.IOException;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Josue
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Inject
    OAuthControl control;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String returnTo = request.getParameter("return_to");

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        //TODO add fail cases
        User foundUser = control.loginUser(username, password);
        if (foundUser != null) {
            //Set this user as logged in
            request.getSession().setAttribute(AuthorizeServlet.USER_PARAM, foundUser);

            //issue a request with retrieved parameters
            String redirectUri = request.getParameter("redirect_uri");
            String clientId = request.getParameter("client_id");

            //Now REDIRECT to the app.jsp, this time logged in...
            //redirect because we need to finish this request and start a new one based on the return_to
            response.sendRedirect(request.getContextPath() + returnTo + "?client_id=" + clientId + "&redirect_uri=" + redirectUri);
        }

    }
    private static final Logger LOG = Logger.getLogger(LoginServlet.class.getName());

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
