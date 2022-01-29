package com.finalexam.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalexam.DB.*;
import com.finalexam.Services.JobService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("/job")
public class JobController {

    @EJB
    private JobService jobService;

    @EJB
    private QueueMessage queueMessage;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewJobCenters")
    @Produces("application/json")
    public Response getJobCenters () throws Exception {

        return Response.ok(
                new ObjectMapper().writeValueAsString(jobService.getCenters())
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewCenterJobs")
    @Produces("application/json")
    public Response getJobsByCenter (
            @QueryParam("center_id") int center_id
    ) throws Exception {


        List<Job> centerJobs = jobService.getJobsByCenter(center_id);

        if(!centerJobs.isEmpty()){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(centerJobs)
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
    @Path("/requestJob")
    @Produces("application/json")
    public Response requestJob (
            @QueryParam("user_id") int user_id,
            @QueryParam("job_id") int job_id
    ) throws Exception {

        Users users = jobService.requestJob(job_id, user_id);

        if(users!=null){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(users)
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
    @Path("/receiveMessage")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getMessage() throws Exception {

        String receiveMessage = queueMessage.receiveMessage();

        return  Response.ok()
                .entity(receiveMessage)
                .build();
    }

}
