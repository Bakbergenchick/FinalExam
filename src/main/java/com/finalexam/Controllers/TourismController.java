package com.finalexam.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalexam.DB.CustomError;
import com.finalexam.DB.Session;
import com.finalexam.Services.TourismService;

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
import java.util.Set;

@Path("/tourism")
public class TourismController {

    @EJB
    private TourismService tourismService;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewCinemas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewCinemas() throws Exception{

        return Response.ok(
                new ObjectMapper().writeValueAsString(tourismService.viewCinemas())
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewFilms")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewFilms(
            @QueryParam("cin_id") int cin_id
    ) throws Exception{


        Map<String, List<Session>> cinemaFilms = tourismService.viewSession(cin_id);

        if(!cinemaFilms.isEmpty()){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(cinemaFilms)
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
    @Path("/buyTicket")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyTicket(
            @QueryParam("user_id") int user_id,
            @QueryParam("ses_id") int ses_id
    ) throws Exception{


        Map<String, List<Session>> cinemaFilms = tourismService.buyTicket(user_id, ses_id);

        if(!cinemaFilms.isEmpty()){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(cinemaFilms)
            ).build();
        }
        else {
            return Response.status(500).entity(
                    new CustomError("Null pointer exception", 500)
            ).build();
        }
    }
}
