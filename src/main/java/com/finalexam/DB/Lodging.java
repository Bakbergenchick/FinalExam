package com.finalexam.DB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Lodging {
    @Id
    private int lod_id;
    private String lod_name;
    private String lod_location;

}
