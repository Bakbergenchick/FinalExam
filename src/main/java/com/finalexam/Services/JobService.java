package com.finalexam.Services;

import com.finalexam.DB.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;

@Stateless
public class JobService {

    @EJB
    private CreateDB createDB;

    public List<JobCenter> getCenters() throws SQLException {
        return createDB.getAllCenters();
    }

    public List<Job> getJobsByCenter(int center_id) throws SQLException{
        return createDB.getJobsByCenterID(center_id);
    }

    public Users requestJob(int job_id, int user_id) throws Exception {
        return createDB.requestJobByID(job_id, user_id);
    }

}
