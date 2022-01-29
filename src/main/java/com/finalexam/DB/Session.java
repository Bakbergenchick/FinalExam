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
public class Session {
    @Id
    private int ses_id;
    private String start;
    private String film_name;
    private int seat;
    private int row;
    private int day;
    private boolean isTaked;
}
