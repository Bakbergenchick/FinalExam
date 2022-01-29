package com.finalexam.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalexam.DB.CustomError;
import com.finalexam.DB.Premises;
import com.finalexam.DB.Session;
import com.finalexam.Services.BusinessService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/business")
@RolesAllowed({"ADMIN", "USER"})
public class BusinessController {

    @EJB
    private BusinessService businessService;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewLodges")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewLodges() throws Exception{

        return Response.ok(
                new ObjectMapper().writeValueAsString(businessService.getLodges())
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewLodgePremises")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewPremises(
            @QueryParam("lod_id") int lod_id
    ) throws Exception{


        Map<String, List<Premises>> lodgePremises = businessService.getPremises(lod_id);

        if(!lodgePremises.isEmpty()){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(lodgePremises)
            ).build();
        }
        else {
            return Response.status(500).entity(
                    new CustomError("Null pointer exception", 500)
            ).build();
        }
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/setPremise")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewPremises(
            @QueryParam("user_id") int user_id,
            @QueryParam("pr_id") int pr_id
    ) throws Exception{


        try {
            Map<String, List<Premises>> setPremises = businessService.setPremises(user_id,pr_id);

                return Response.ok(
                        new ObjectMapper().writeValueAsString(setPremises)
                ).build();

        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                    new CustomError("Null pointer exception", 500)
            ).build();
        }

    }

    @GET
    @Path("/getCheckList")
    @RolesAllowed({"ADMIN", "USER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCheck(
            @QueryParam("user_id") int user_id
    ) throws Exception{


        Map<String, String> setPremises = businessService.getCheckList(user_id);

        if(!setPremises.isEmpty()){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(setPremises)
            ).build();
        }
        else {
            return Response.status(500).entity(
                    new CustomError("Null pointer exception", 500)
            ).build();
        }
    }


}
