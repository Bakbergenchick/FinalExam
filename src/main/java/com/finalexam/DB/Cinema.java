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
public class Cinema {
    @Id
    private int cin_id;
    private String cin_name;
    private String cin_add;
}
