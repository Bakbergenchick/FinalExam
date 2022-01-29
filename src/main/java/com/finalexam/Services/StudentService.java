package com.finalexam.Services;

import com.finalexam.DB.CreateDB;
import com.finalexam.DB.Library;
import com.finalexam.DB.Users;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Stateless
public class StudentService {
    @EJB
    private CreateDB createDB;


    public List<Library> getLibraries() throws SQLException {
        return createDB.getAllLibraries();
    }

    public Users getUserID(int id) throws SQLException {
        return createDB.getUserById(id);
    }

    public Library getLibraryID(int id) throws SQLException {
        return createDB.getLibraryById(id);
    }

    public void SetUserToLib(int user_id, int lib_id) throws SQLException{
        createDB.setUserToLib(user_id, lib_id);
    }

    public Map<String, Set<String>> getUserLibs(int user_id) throws Exception{
        return createDB.getUserLib(user_id);
    }

    public Map<String, Set<String>> getUserBooks(int user_id) throws Exception{
        return createDB.getLibBook(user_id);
    }
}
