package com.finalexam.Services;

import com.finalexam.DB.CreateDB;
import com.finalexam.DB.Lodging;
import com.finalexam.DB.Premises;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.Map;

@Stateless
public class BusinessService {

    @EJB
    private CreateDB createDB;

    public List<Lodging> getLodges() throws Exception {
        return createDB.getAllLodges();
    }

    public Map<String, List<Premises>> getPremises(int lod_id) throws Exception {
        return createDB.getPremises(lod_id);
    }

    public Map<String, List<Premises>> setPremises(int user_id, int pr_id) throws Exception {
        return createDB.setPremise(user_id, pr_id);
    }

    public Map<String, String> getCheckList(int user_id) throws Exception{
        return createDB.getCheckList(user_id);
    }
}
