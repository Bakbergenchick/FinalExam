package com.finalexam.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalexam.DB.CustomError;
import com.finalexam.DB.Library;
import com.finalexam.DB.Users;
import com.finalexam.Services.StudentService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@Path("/student")
public class StudentController {

    @EJB
    private StudentService studentService;


    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewLibraries")
    @Produces("application/json")
    public Response viewLibraries() throws Exception {

        return Response.ok(
                new ObjectMapper().writeValueAsString(studentService.getLibraries()))
                .build();

    }

    @POST
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/regToLibrary")
    public Response registrToLibrary(
            @QueryParam("lib_id") int lib_id,
            @QueryParam("user_id") int user_id
    ) throws SQLException {

        Users user = null;
        Library library = null;

        user = studentService.getUserID(user_id);
        library = studentService.getLibraryID(lib_id);

        if(user!=null && library!=null){
            studentService.SetUserToLib(user.getUser_id(), library.getLib_id());
        } else{
            return Response.status(500).entity(
                    new CustomError("Null pointer exception", 500)
            ).build();
        }


        return Response.ok().entity("Registered!").build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/viewMyLibraries")
    @Produces("application/json")
    public Response MyLib(
            @QueryParam("user_id") int user_id
    ) throws Exception {

        Map<String, Set<String>> userLibs = studentService.getUserLibs(user_id);

        if(!userLibs.isEmpty()){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(userLibs)
            ).build();
        }
        else {
            return Response.status(500).entity(
                    new CustomError("Null pointer exception", 500)
            ).build();
        }

    }

    @GET
    @Path("/viewMyBooks")
    @RolesAllowed({"ADMIN", "USER"})
    @Produces("application/json")
    public Response MyBooks(
            @QueryParam("user_id") int user_id
    ) throws Exception {


        Map<String, Set<String>> userBooks = studentService.getUserBooks(user_id);

        if(!userBooks.isEmpty()){
            return Response.ok(
                    new ObjectMapper().writeValueAsString(userBooks)
            ).build();
        }
        else {
            return Response.status(500).entity(
                    new CustomError("Null pointer exception", 500)
            ).build();
        }

    }
}