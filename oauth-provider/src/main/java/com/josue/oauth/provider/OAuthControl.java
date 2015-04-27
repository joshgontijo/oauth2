/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.Application;
import com.josue.oauth.provider.entity.Authorization;
import com.josue.oauth.provider.entity.User;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 *
 * @author Josue
 */
public class OAuthControl {

    @PersistenceContext
    EntityManager em;

    private <T> T getSingleResult(Query query) {
        List<T> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    public Application fetchApplication(String clientId) {
        TypedQuery<Application> query = em.createQuery("SELECT app FROM Application app WHERE app.clientId = :clientId", Application.class);
        query.setParameter("clientId", clientId);
        Application foundApp = getSingleResult(query);
        return foundApp;
    }

    public Application validateCredentials(String clientId, String clientSecret) throws Exception {
        TypedQuery<Application> query = em.createQuery("SELECT app FROM Application app WHERE app.clientId = :clientId AND app.appSecret = :clientSecret", Application.class);
        query.setParameter("clientId", clientId);
        query.setParameter("clientSecret", clientSecret);
        Application foundApp = query.getSingleResult();
        return foundApp;
    }

    public User loginUser(String username, String password) {
        if (username != null && password != null) {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.login = :login AND u.password = :password", User.class);
            query.setParameter("login", username);
            query.setParameter("password", password);
            User foundUsefouUser = getSingleResult(query);
            return foundUsefouUser;
        }
        return null;
    }

    public User fetchUser(String login) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class);
        query.setParameter("login", login);
        User foundUsefouUser = getSingleResult(query);
        return foundUsefouUser;

    }

    public Authorization fetchAuthorization(String code) {
        TypedQuery<Authorization> query = em.createQuery("SELECT auth FROM Authorization auth WHERE auth.code = :code", Authorization.class);
        query.setParameter("code", code);
        Authorization foundAuthorization = getSingleResult(query);
        return foundAuthorization;
    }

    @Transactional
    public Authorization createAuthorization(String clientId, String userLogin) {
        Application foundApp = fetchApplication(clientId);
        User foundUser = fetchUser(userLogin);
        if (foundApp != null && foundUser != null) {
            String code = UUID.randomUUID().toString();
            int expiresIn = 2;

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, expiresIn);

            Authorization auth = new Authorization(null, code, cal.getTime(), foundApp, foundUser);
            em.persist(auth);
            return auth;
        }
        throw new RuntimeException("User or Application not found");

    }

}
