package com.finalexam.DB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JobCenter {
    @Id
    private int job_center_id;
    private String center_name;
    private String center_location;
    private String center_contact;
}
