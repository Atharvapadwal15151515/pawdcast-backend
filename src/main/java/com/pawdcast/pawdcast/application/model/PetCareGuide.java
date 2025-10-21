package com.pawdcast.pawdcast.application.model;

import java.util.List;
import java.util.Map;

public class PetCareGuide {
    private String projectTitle;
    private String description;
    private String lastUpdated;
    private List<PetType> petTypes;

    // Getters and Setters
    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<PetType> getPetTypes() {
        return petTypes;
    }

    public void setPetTypes(List<PetType> petTypes) {
        this.petTypes = petTypes;
    }

    // Inner static class for Pet Types
    public static class PetType {
        private String species;
        private String scientificName;
        private Map<String, Object> overview;
        private Map<String, Object> healthConsiderations;
        private Map<String, Object> nutrition;
        private Map<String, Object> groomingAndHygiene;
        private Map<String, Object> financialConsiderations;
        private Map<String, Object> dosAndDonts;

        // Getters and Setters
        public String getSpecies() {
            return species;
        }

        public void setSpecies(String species) {
            this.species = species;
        }

        public String getScientificName() {
            return scientificName;
        }

        public void setScientificName(String scientificName) {
            this.scientificName = scientificName;
        }

        public Map<String, Object> getOverview() {
            return overview;
        }

        public void setOverview(Map<String, Object> overview) {
            this.overview = overview;
        }

        public Map<String, Object> getHealthConsiderations() {
            return healthConsiderations;
        }

        public void setHealthConsiderations(Map<String, Object> healthConsiderations) {
            this.healthConsiderations = healthConsiderations;
        }

        public Map<String, Object> getNutrition() {
            return nutrition;
        }

        public void setNutrition(Map<String, Object> nutrition) {
            this.nutrition = nutrition;
        }

        public Map<String, Object> getGroomingAndHygiene() {
            return groomingAndHygiene;
        }

        public void setGroomingAndHygiene(Map<String, Object> groomingAndHygiene) {
            this.groomingAndHygiene = groomingAndHygiene;
        }

        public Map<String, Object> getFinancialConsiderations() {
            return financialConsiderations;
        }

        public void setFinancialConsiderations(Map<String, Object> financialConsiderations) {
            this.financialConsiderations = financialConsiderations;
        }

        public Map<String, Object> getDosAndDonts() {
            return dosAndDonts;
        }

        public void setDosAndDonts(Map<String, Object> dosAndDonts) {
            this.dosAndDonts = dosAndDonts;
        }
    }
}
