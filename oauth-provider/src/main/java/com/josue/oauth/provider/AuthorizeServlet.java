/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.Application;
import com.josue.oauth.provider.entity.Authorization;
import com.josue.oauth.provider.entity.OAuthParam;
import com.josue.oauth.provider.entity.User;
import com.josue.oauth.provider.ex.InvalidClientCredException;
import com.josue.oauth.provider.ex.OAuthException;
import com.josue.oauth.provider.ex.URLMismatchExcetion;
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

    //internal (non-oauth) to check the user session
    public static final String LOGGED_USER = "user";
    //This is used as a internal (non-oauth) callback url in not logged in cases
    public static final String RETURN_TO = "return_to";
    //Param variable that define if the user granted access to the client application
    private static final String USER_GRANT = "grant";

    //Possible values for the form submission
    private static final String USER_GRANT_GRANTED = "granted";

    //This servlet URL
    private static final String AUTHORIZE_URL = "/authorize";
    private static final String AUTHORIZATION_JSP = "/app.jsp";
    private static final String LOGIN_JSP = "/login.jsp";
    private static final String APPLICATION_PARAM = "app";

    @Inject
    private OAuthControl control;

    // example from github (note the return_to param for the post login process
    //https://github.com/login?return_to=/login/oauth/authorize?client_id=152951b17f452be3a025&redirect_uri=https://oauth.io/auth&response_type=code&scope=user:email&state=mSztGcaJLf1VvInr7mKwYs3nRnI
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Gather the URL param sent by the client OR from the LoginServlet (not logged users)
        String clientId = request.getParameter(OAuthParam.CLIENT_ID);
        String redirectUri = request.getParameter(OAuthParam.REDIRECT_URL);
        String scope = request.getParameter(OAuthParam.SCOPE);
        String state = request.getParameter(OAuthParam.STATE);

        //Add to the internal server attributes, so we can catch later (after app.jsp form submission)
        request.setAttribute(OAuthParam.CLIENT_ID, clientId);
        request.setAttribute(OAuthParam.REDIRECT_URL, redirectUri);
        request.setAttribute(OAuthParam.SCOPE, scope);
        request.setAttribute(OAuthParam.STATE, state);

        //validate input
        if (clientId == null || redirectUri == null) {
            return; //nothing to do
        }

        //Attributes should be stored to the session, not to the request, since login attempt can fail
        if (request.getSession().getAttribute(LOGGED_USER) == null) { //user not logged in

            request.setAttribute(RETURN_TO, AUTHORIZE_URL);//return to this servlet, but now logged in
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
        } else {// here the session should contains a user
            Application foundApplication = control.fetchApplication(clientId);
            if (foundApplication == null) {
                sendErrorResponse(response, redirectUri, new InvalidClientCredException());
                return;
            } else if (!foundApplication.getRedirectUrl().equals(redirectUri)) {
                sendErrorResponse(response, redirectUri, new URLMismatchExcetion());
                return;
            }
            request.setAttribute(APPLICATION_PARAM, foundApplication);
            request.getRequestDispatcher(AUTHORIZATION_JSP).forward(request, response);
        }
    }

    @Override// The POST method handle the user's confirmation
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //form params

        //Gathering form params: 'clientId' and 'redirectUrl' are set up in this Servlet
        String clientId = request.getParameter(OAuthParam.CLIENT_ID);
        String redirectUrl = request.getParameter(OAuthParam.REDIRECT_URL);//TODO request should receive redirect url
        String granted = request.getParameter(USER_GRANT);

        User loggedUser = (User) request.getSession().getAttribute(LOGGED_USER);
        if (USER_GRANT_GRANTED.equals(granted)) {

            Authorization createdAuthorization = control.createAuthorization(clientId, loggedUser.getLogin());
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", redirectUrl + "?code=" + createdAuthorization.getCode());

        } else {//user denied access, return error message code to the client by URL
            //ref: https://developer.github.com/v3/oauth/#common-errors-for-the-authorization-request

            //TODO access denied
        }
    }
    /*
     * Note that during the CODE request flow stage, the provider will return status
     * code through the URL, because the redirection (obviously)........ In the
     * ACCESS TOKEN request flow stage the server will return response by json since
     * is direct communication with the client app
     */

    private void sendErrorResponse(HttpServletResponse response, String redirectUrl, OAuthException exception) {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", redirectUrl + "?error=" + exception.getError() + "&description=" + exception.getMessage());
    }
}
