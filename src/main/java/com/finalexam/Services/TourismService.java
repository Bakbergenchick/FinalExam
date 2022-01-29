package com.finalexam.Services;

import com.finalexam.DB.Cinema;
import com.finalexam.DB.CreateDB;
import com.finalexam.DB.Session;
import com.finalexam.DB.Users;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Stateless
public class TourismService {

    @EJB
    private CreateDB createDB;

    public List<Cinema> viewCinemas() throws Exception {
        return createDB.viewAllCinemas();
    }

    public Map<String, List<Session>> viewSession(int cin_id) throws Exception {
        return createDB.viewAllSession(cin_id);
    }

    public Map<String, List<Session>> buyTicket(int user_id,int ses_id) throws Exception {
        return createDB.buyTicket(user_id, ses_id);
    }
}
