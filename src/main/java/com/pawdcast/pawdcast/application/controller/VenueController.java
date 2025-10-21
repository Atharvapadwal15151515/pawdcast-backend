package com.pawdcast.pawdcast.application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pawdcast.pawdcast.application.dao.VenueDAO;
import com.pawdcast.pawdcast.application.model.Venue;

@RestController
@RequestMapping("/venues")
@CrossOrigin(origins = "*")
public class VenueController {

    private final VenueDAO dao;

  
    public VenueController(VenueDAO dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<Venue> getVenues(
            @RequestParam(name = "area", required = false, defaultValue = "Mumbai") String area,
            @RequestParam(name = "locality", required = false) String locality,
            @RequestParam(name = "type", required = false) String type
    ) {
        if (locality != null && !locality.isBlank() && type != null && !type.isBlank()) {
            return dao.getVenuesByAreaAndLocalityAndType(area, locality, type);
        } else if (locality != null && !locality.isBlank()) {
            return dao.getVenuesByAreaAndLocality(area, locality);
        } else if (type != null && !type.isBlank()) {
            return dao.getVenuesByAreaAndType(area, type);
        } else {
            return dao.getVenuesByArea(area);
        }
    }
}
