package com.pawdcast.pawdcast.application.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawdcast.pawdcast.application.model.PetCareGuide;

@Service
public class PetCareService {

    public PetCareGuide loadPetCareGuide() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pet-care.json");
        return mapper.readValue(inputStream, PetCareGuide.class);
    }
}
