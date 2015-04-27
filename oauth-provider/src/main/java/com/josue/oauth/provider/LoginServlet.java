/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.OAuthParam;
import com.josue.oauth.provider.entity.User;
import java.io.IOException;
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

        String returnTo = request.getParameter(AuthorizeServlet.RETURN_TO);

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        //*** Catch params from AuthorizeServlet or from this servlet in cases of login failure
        //issue a request with retrieved parameters
        String redirectUri = request.getParameter(OAuthParam.REDIRECT_URL);
        String clientId = request.getParameter(OAuthParam.CLIENT_ID);
        String scope = request.getParameter(OAuthParam.SCOPE);
        String state = request.getParameter(OAuthParam.STATE);

        //TODO add fail cases
        User foundUser = control.loginUser(username, password);
        if (foundUser != null) {
            //Set this user as logged in
            request.getSession().setAttribute(AuthorizeServlet.LOGGED_USER, foundUser);

            //Now REDIRECT to the app.jsp, this time logged in...
            //redirect because we need to finish this request and start a new one based on the return_to
            response.sendRedirect(request.getContextPath() + returnTo
                    + "?client_id=" + clientId
                    + "&redirect_uri=" + redirectUri
                    + "&scope=" + scope
                    + "&state=" + state);
        } else { //Login failed, lets set the attributes again, so we can catch it next login attempt

            //Set attribute(internal server) so we can catch it again in this servlet...
            //Why ?
            //http://stackoverflow.com/questions/5243754/difference-between-getattribute-and-getparameter
            request.setAttribute(OAuthParam.CLIENT_ID, clientId);
            request.setAttribute(OAuthParam.REDIRECT_URL, redirectUri);
            request.setAttribute(OAuthParam.SCOPE, scope);
            request.setAttribute(OAuthParam.STATE, state);
            request.setAttribute(AuthorizeServlet.RETURN_TO, returnTo);

            request.setAttribute("loginErrorMessage", "Invalid credentials");

            request.getRequestDispatcher("/login.jsp").include(request, response);
        }

    }
}
