package com.finalexam.Services;

import com.finalexam.DB.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AdminService {

    @EJB
    private CreateDB createDB;


    public List<Users> getUsers() throws Exception{
        return createDB.getAllUsers();
    }

    public List<Book> getBooks() throws Exception{
        return createDB.getAllBooks();
    }

    public List<Job> getJobs() throws Exception{
        return createDB.getAllJobs();
    }

    public List<Film> getFilms() throws Exception{
        return createDB.getAllFilms();
    }

    public void addUser(String fullname, String email, String password, String birthdate) throws Exception{
       createDB.addUser(fullname, email, password, birthdate);
    }

    public void addLib(String lib_name, String location) throws Exception{
        createDB.addLibrary(lib_name, location);
    }

    public void delUser(int user_id) throws Exception{
        createDB.deleteUser(user_id);
    }

    public void delBook(int book_id) throws Exception{
        createDB.deleteBook(book_id);
    }

    public void delFilm(int film_id) throws Exception{
        createDB.deleteFilm(film_id);
    }

    public void editUsers(int user_id, String email) throws Exception{
        createDB.editUser(user_id, email);
    }
}
