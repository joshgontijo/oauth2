/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.josue.oauth.resource;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Josue
 */
@Path("generic")
@RequestScoped
public class GenericResource {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Path("action1")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericEntity action1(@HeaderParam("Authorization") String authToken) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @GET
    @Path("action1")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericEntity action2(@HeaderParam("Authorization") String authToken) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @GET
    @Path("action1")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericEntity action3(@HeaderParam("Authorization") String authToken) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private boolean hasAccess(String token) {
        return false;
    }

}
