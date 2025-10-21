package com.pawdcast.pawdcast.application.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawdcast.pawdcast.application.model.Pet;

import jakarta.annotation.PostConstruct;

@Service
public class PetService {

    private List<Pet> pets = new ArrayList<>();

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Path to your JSON file
            File file = new File("src/main/resources/pets.json");
            JsonNode rootNode = mapper.readTree(file);

            // Load dogs
            JsonNode dogsNode = rootNode.get("dogs");
            if (dogsNode != null && dogsNode.isArray()) {
                for (JsonNode dogNode : dogsNode) {
                    Pet dog = mapper.treeToValue(dogNode, Pet.class);
                    dog.setType("dog");
                    pets.add(dog);
                }
            }

            // Load cats
            JsonNode catsNode = rootNode.get("cats");
            if (catsNode != null && catsNode.isArray()) {
                for (JsonNode catNode : catsNode) {
                    Pet cat = mapper.treeToValue(catNode, Pet.class);
                    cat.setType("cat");
                    pets.add(cat);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pet> getAllPets() {
        return pets;
    }

    public List<Pet> getPetsByType(String type) {
        List<Pet> filtered = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getType().equalsIgnoreCase(type)) {
                filtered.add(pet);
            }
        }
        return filtered;
    }
}