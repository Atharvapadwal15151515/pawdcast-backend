package com.pawdcast.pawdcast.application.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.Venue; 
 
@Repository 
public class VenueDAO { 
 
    private final JdbcTemplate jdbc; 
 
    public VenueDAO(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    } 
 
    private final RowMapper<Venue> rowMapper = (rs, rowNum) -> new Venue( 
            rs.getInt("id"), 
            rs.getString("name"), 
            rs.getString("area"), 
            rs.getString("locality"), 
            rs.getDouble("latitude"), 
            rs.getDouble("longitude"), 
            rs.getString("type") 
    ); 
 
    public List<Venue> getVenuesByArea(String area) { 
        String sql = "SELECT id, name, area, locality, latitude, longitude, type FROM pet_venues WHERE area = ?"; 
        return jdbc.query(sql, rowMapper, area); 
    } 
 
    public List<Venue> getVenuesByAreaAndLocality(String area, String 
locality) { 
        String sql = "SELECT id, name, area, locality, latitude, longitude, type FROM pet_venues WHERE area = ? AND locality = ?"; 
        return jdbc.query(sql, rowMapper, area, locality); 
    } 
 
    public List<Venue> getVenuesByAreaAndLocalityAndType(String area, String 
locality, String type) { 
        String sql = "SELECT id, name, area, locality, latitude, longitude, type FROM pet_venues WHERE area = ? AND locality = ? AND type = ?"; 
        return jdbc.query(sql, rowMapper, area, locality, type); 
    } 
 
    public List<Venue> getVenuesByAreaAndType(String area, String type) { 
        String sql = "SELECT id, name, area, locality, latitude, longitude, type FROM pet_venues WHERE area = ? AND type = ?"; 
        return jdbc.query(sql, rowMapper, area, type); 
    } 
} 