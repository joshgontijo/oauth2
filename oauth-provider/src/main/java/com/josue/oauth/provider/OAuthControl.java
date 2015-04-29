/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.provider;

import com.josue.oauth.provider.entity.AccessToken;
import com.josue.oauth.provider.entity.Application;
import com.josue.oauth.provider.entity.Authorization;
import com.josue.oauth.provider.entity.User;
import com.josue.oauth.provider.ex.BadVerificationCodeException;
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

    @Transactional
    public Authorization validateAuthorization(String code, String clientId, String clientSecret) throws BadVerificationCodeException {
        TypedQuery<Authorization> query = em.createQuery("SELECT auth FROM Authorization auth WHERE auth.code = :code", Authorization.class);
        query.setParameter("code", code);
        Authorization foundAuthorization = getSingleResult(query);
        if (foundAuthorization != null) {
            if (foundAuthorization.getExpires().before(new Date())) {
                throw new BadVerificationCodeException();
            } else if (foundAuthorization.getStatus() == Authorization.Status.INACTIVE
                    || foundAuthorization.getStatus() == Authorization.Status.APPLIED) {
                //revoke the access token in case of multiple request by the same code
                invalidateAccessTokenByAuthorization(foundAuthorization);
                throw new BadVerificationCodeException();
            } else if (!foundAuthorization.getApplication().getClientId().equals(clientId)
                    || !foundAuthorization.getApplication().getAppSecret().equals(clientSecret)) {
                foundAuthorization.setStatus(Authorization.Status.INACTIVE);
                em.merge(foundAuthorization);
                throw new BadVerificationCodeException();
            }
        } else {
            throw new BadVerificationCodeException();
        }
        return foundAuthorization;
    }

    @Transactional
    public Authorization createAuthorization(String clientId, String userLogin
    ) {
        Application foundApp = fetchApplication(clientId);
        User foundUser = fetchUser(userLogin);
        if (foundApp != null && foundUser != null) {
            String code = UUID.randomUUID().toString();
            int expiresIn = 2;

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, expiresIn);

            Authorization auth = new Authorization(code, cal.getTime(), foundApp, foundUser, Authorization.Status.ACTIVE);
            em.persist(auth);
            return auth;
        }
        throw new RuntimeException("User or Application not found");
    }

    @Transactional
    public void invalidateAuthorization(Authorization auth) {
        em.refresh(auth);
        auth.setStatus(Authorization.Status.INACTIVE);
    }

    @Transactional
    public void invalidateAccessTokenByAuthorization(Authorization auth) {
        TypedQuery<AccessToken> query = em.createQuery("SELECT at FROM AccessToken at WHERE at.authorization.code = :code", AccessToken.class);
        query.setParameter("code", auth.getCode());
        AccessToken accessToken = getSingleResult(query);
        if (accessToken.getStatus() == AccessToken.Status.ACTIVE) {
            accessToken.setStatus(AccessToken.Status.REVOKED);
            em.merge(accessToken);
        }
    }

    @Transactional
    public void invalidateAccessTokenByToken(Integer accessTokenId) {
        AccessToken foundAccessToken = em.find(AccessToken.class, accessTokenId);
        if (foundAccessToken != null) {
            if (foundAccessToken.getStatus() == AccessToken.Status.ACTIVE) {
                foundAccessToken.setStatus(AccessToken.Status.REVOKED);
                em.merge(foundAccessToken);
            }
        }
    }

    @Transactional
    public AccessToken createAccessToken(Authorization authorization) {
        //Generate Token response
        String token = String.valueOf(UUID.randomUUID());
        String refreshToken = String.valueOf(UUID.randomUUID());
        int expiresIn = 86400;

        AccessToken accessToken = new AccessToken();
        accessToken.setAuthorization(authorization);
        accessToken.setResourceOwner(authorization.getUser());
        accessToken.setExpiresIn(expiresIn);
        accessToken.setRefreshToken(refreshToken);
        accessToken.setToken(token);
        accessToken.setApplication(authorization.getApplication());
        accessToken.setStatus(AccessToken.Status.ACTIVE);

        Authorization contextAuthorization = em.find(Authorization.class, authorization.getId());
        contextAuthorization.setStatus(Authorization.Status.APPLIED);

        em.persist(accessToken);
        return accessToken;
    }

    public boolean alreadyAuthorized(Integer userId, Integer clientAppId) {
        TypedQuery<AccessToken> query = em.createQuery("SELECT at FROM AccessToken at WHERE at.resourceOwner.id = :userId AND at.application.id = :clientAppId", AccessToken.class);
        query.setParameter("userId", userId);
        query.setParameter("clientAppId", clientAppId);
        return getSingleResult(query) != null;
    }

    public List<AccessToken> listAuthorizedApplications(Integer userId) {
        TypedQuery<AccessToken> query = em.createQuery("SELECT at FROM AccessToken at WHERE at.resourceOwner.id = :id", AccessToken.class);
        query.setParameter("id", userId);
        List<AccessToken> resultList = query.getResultList();
        return resultList;
    }

}
