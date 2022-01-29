package com.finalexam.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalexam.DB.QueueMessage;
import com.finalexam.DB.Users;
import com.finalexam.Services.AdminService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
public class AdminController {

    @EJB
    private AdminService adminService;

    @EJB
    private QueueMessage queueMessage;

    // Get Methods
    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/viewUsers")
    @Produces("application/json")
    public Response getAllUsers() throws Exception {

        return Response.ok(
                new ObjectMapper().writeValueAsString(adminService.getUsers())
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/viewBooks")
    @Produces("application/json")
    public Response getAllBooks() throws Exception {

        return Response.ok(
                new ObjectMapper().writeValueAsString(adminService.getBooks())
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/viewJobs")
    @Produces("application/json")
    public Response getAllJobs() throws Exception {

        return Response.ok(
                new ObjectMapper().writeValueAsString(adminService.getJobs())
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/viewFilms")
    @Produces("application/json")
    public Response getAllFilms() throws Exception {

        return Response.ok(
                new ObjectMapper().writeValueAsString(adminService.getFilms())
        ).build();
    }

    // POST Methods
    @POST
    @RolesAllowed({"ADMIN"})
    @Path("/addUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(
            @FormParam("fullname") String fullname,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("birthdate") String birthdate
    ) throws Exception{

        adminService.addUser(fullname, email, password, birthdate);

        return Response.ok().entity("User added!").build();
    }

    @POST
    @RolesAllowed({"ADMIN"})
    @Path("/addLibrary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLibrary(
            @FormParam("libname") String libname,
            @FormParam("location") String location
    ) throws Exception{

        adminService.addLib(libname,location);

        return Response.ok().entity("Library added!").build();
    }

    @DELETE
    @RolesAllowed({"ADMIN"})
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(
            @QueryParam("user_id") int user_id
    ) throws Exception{
        adminService.delUser(user_id);

        return Response.ok().entity("User with id: " + user_id + " deleted!").build();
    }

    @DELETE
    @RolesAllowed({"ADMIN"})
    @Path("/deleteBook")

    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(
            @QueryParam("book_id") int book_id
    ) throws Exception{
        adminService.delBook(book_id);

        return Response.ok().entity("Book with id: " + book_id + " deleted!").build();
    }

    @DELETE
    @RolesAllowed({"ADMIN"})
    @Path("/deleteFilm")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFilm(
            @QueryParam("film_id") int film_id
    ) throws Exception{
        adminService.delFilm(film_id);

        return Response.ok().entity("Film with id: " + film_id + " deleted!").build();
    }

    @PUT
    @RolesAllowed({"ADMIN"})
    @Path("/editUser")

    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(
            @FormParam("user_id") int user_id,
            @FormParam("email") String email
    ) throws Exception{

        adminService.editUsers(user_id, email);

        return Response.ok().entity("User with id: " + user_id+ " changed!").build();
    }

    // JMS
    @POST
    @RolesAllowed({"ADMIN"})
    @Path("/sendMessage")
    @Produces("application/json")
    @Consumes("application/json")
    public Response sendMessage(String raw) throws Exception {

        Users user = null;

        try{
            user = new ObjectMapper().readValue(raw, Users.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        queueMessage.sendMessage(raw);

        return Response.ok().entity(user).build();
    }


}
