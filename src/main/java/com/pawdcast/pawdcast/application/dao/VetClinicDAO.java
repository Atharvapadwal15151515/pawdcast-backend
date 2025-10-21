package com.pawdcast.pawdcast.application.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.VetClinic; 

@Repository 
public class VetClinicDAO { 

   private final JdbcTemplate jdbc; 

   public VetClinicDAO(JdbcTemplate jdbc) { 
       this.jdbc = jdbc; 
   } 

   private final RowMapper<VetClinic> rowMapper = (rs, rowNum) -> new 
VetClinic( 
           rs.getInt("id"), 
           rs.getString("name"), 
           rs.getString("area"), 
           rs.getString("locality"), 
           rs.getDouble("latitude"), 
           rs.getDouble("longitude") 
   ); 

   public List<VetClinic> getClinicsByArea(String area) { 
       String sql = "SELECT id, name, area, locality, latitude, longitude FROM vet_clinics WHERE area = ?"; 
       return jdbc.query(sql, rowMapper, area); 
   } 

   public List<VetClinic> getClinicsByAreaAndLocality(String area, String 
locality) { 
       String sql = "SELECT id, name, area, locality, latitude, longitude FROM vet_clinics WHERE area = ? AND locality = ?"; 
       return jdbc.query(sql, rowMapper, area, locality); 
   } 
} 
