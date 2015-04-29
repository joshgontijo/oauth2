/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.resource;

import static com.josue.oauth.provider.AuthorizeServlet.LOGGED_USER;
import com.josue.oauth.provider.OAuthControl;
import com.josue.oauth.provider.entity.AccessToken;
import com.josue.oauth.provider.entity.User;
import java.io.IOException;
import java.util.List;
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
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Inject
    private OAuthControl control;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = (User) request.getSession().getAttribute(LOGGED_USER);
        if (loggedUser == null) {
            request.getRequestDispatcher("/login.jsp").include(request, response);
            return;
        }
        List<AccessToken> tokens = control.listAuthorizedApplications(loggedUser.getId());
        request.setAttribute("tokenz", tokens);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = (String) request.getParameter("action");
        String tokenId = (String) request.getParameter("tokenId");
        if ("revoke".equals(action) && !"".equals(tokenId)) {
            control.invalidateAccessTokenByToken(Integer.valueOf(tokenId));
        } else {
            processRequest(request, response);
        }
    }
}
