/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.Authorization;
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
@WebServlet(name = "AuthorizeServlet", urlPatterns = {"/authorize"})
public class AuthorizeServlet extends HttpServlet {

    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String REDIRECT_URL_PARAM = "redirect_uri";

    public static final String USER_PARAM = "user";
    public static final String RETURN_TO = "return_to";

    @Inject
    OAuthControl control;

    // example from github (note the return_to param for the post login process
    //https://github.com/login?return_to=/login/oauth/authorize?client_id=152951b17f452be3a025&redirect_uri=https://oauth.io/auth&response_type=code&scope=user:email&state=mSztGcaJLf1VvInr7mKwYs3nRnI
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String clientId = request.getParameter(CLIENT_ID_PARAM);
        String redirectUri = request.getParameter(REDIRECT_URL_PARAM);

        if (request.getSession().getAttribute(USER_PARAM) == null) {
            request.setAttribute(CLIENT_ID_PARAM, clientId);
            request.setAttribute(REDIRECT_URL_PARAM, redirectUri);
            request.setAttribute(RETURN_TO, "/authorize");//return to this servlet, but now logged in

            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // here the session should contains a user
            request.setAttribute("application", control.fetchApplication(clientId));
            request.getRequestDispatcher("app.jsp").forward(request, response);
        }

    }

    @Override// The POST method handle the user's confirmation
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String clientId = request.getParameter(CLIENT_ID_PARAM);
        String granted = request.getParameter("granted");
        String redirectUrl = request.getParameter("redirect_url");//TODO request should receive redirect url

        User loggedUser = (User) request.getSession().getAttribute(USER_PARAM);
        if ("true".equals(granted)) {

            Authorization createdAuthorization = control.createAuthorization(clientId, loggedUser.getLogin());

            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", redirectUrl + "?code=" + createdAuthorization.getCode());
//            response.sendRedirect(redirectUrl + "?code=" + code + "&expires_in=" + expiresIn);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
