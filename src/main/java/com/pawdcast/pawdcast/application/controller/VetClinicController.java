package com.pawdcast.pawdcast.application.controller;
 
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pawdcast.pawdcast.application.dao.VetClinicDAO;
import com.pawdcast.pawdcast.application.model.VetClinic; 
 
@RestController 
@RequestMapping("/clinics") 
@CrossOrigin(origins = "*") // allow CORS for testing; tighten in production 
public class VetClinicController { 
 
    private final VetClinicDAO dao; 
 
    public VetClinicController(VetClinicDAO dao) { 
        this.dao = dao; 
    } 
 
    /** 
     * GET /clinics?area=Mumbai&locality=Andheri 
     * - area defaults to "Mumbai" if not provided 
     * - locality optional 
     */ 
    @GetMapping 
    public List<VetClinic> getClinics( 
            @RequestParam(name = "area", required = false, defaultValue = 
"Mumbai") String area, 
            @RequestParam(name = "locality", required = false) String 
locality 
    ) { 
        if (locality != null && !locality.isBlank()) { 
            return dao.getClinicsByAreaAndLocality(area, locality); 
        } else { 
            return dao.getClinicsByArea(area); 
        } 
    } 
}