package com.pawdcast.pawdcast.application.model;

import java.util.List;

public class PetDietGuidelines {
    private List<RecommendedFoodCategory> recommendedFoods;
    private List<FoodsToAvoidCategory> foodsToAvoid;
    private FeedingGuidelines feedingGuidelines;

    // Constructors
    public PetDietGuidelines() {}

    public PetDietGuidelines(List<RecommendedFoodCategory> recommendedFoods, 
                           List<FoodsToAvoidCategory> foodsToAvoid, 
                           FeedingGuidelines feedingGuidelines) {
        this.recommendedFoods = recommendedFoods;
        this.foodsToAvoid = foodsToAvoid;
        this.feedingGuidelines = feedingGuidelines;
    }

    // Getters and Setters
    public List<RecommendedFoodCategory> getRecommendedFoods() { return recommendedFoods; }
    public void setRecommendedFoods(List<RecommendedFoodCategory> recommendedFoods) { this.recommendedFoods = recommendedFoods; }

    public List<FoodsToAvoidCategory> getFoodsToAvoid() { return foodsToAvoid; }
    public void setFoodsToAvoid(List<FoodsToAvoidCategory> foodsToAvoid) { this.foodsToAvoid = foodsToAvoid; }

    public FeedingGuidelines getFeedingGuidelines() { return feedingGuidelines; }
    public void setFeedingGuidelines(FeedingGuidelines feedingGuidelines) { this.feedingGuidelines = feedingGuidelines; }

    // Inner classes
    public static class RecommendedFoodCategory {
        private String category;
        private List<String> foods;
        private String benefits;

        public RecommendedFoodCategory() {}

        public RecommendedFoodCategory(String category, List<String> foods, String benefits) {
            this.category = category;
            this.foods = foods;
            this.benefits = benefits;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getFoods() { return foods; }
        public void setFoods(List<String> foods) { this.foods = foods; }
        public String getBenefits() { return benefits; }
        public void setBenefits(String benefits) { this.benefits = benefits; }
    }

    public static class FoodsToAvoidCategory {
        private String category;
        private List<String> foods;
        private String dangers;

        public FoodsToAvoidCategory() {}

        public FoodsToAvoidCategory(String category, List<String> foods, String dangers) {
            this.category = category;
            this.foods = foods;
            this.dangers = dangers;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getFoods() { return foods; }
        public void setFoods(List<String> foods) { this.foods = foods; }
        public String getDangers() { return dangers; }
        public void setDangers(String dangers) { this.dangers = dangers; }
    }

    public static class FeedingGuidelines {
        private String puppiesKittens;
        private String adultDogs;
        private String adultCats;
        private String seniorPets;
        private List<String> generalTips;

        public FeedingGuidelines() {}

        public FeedingGuidelines(String puppiesKittens, String adultDogs, String adultCats, 
                               String seniorPets, List<String> generalTips) {
            this.puppiesKittens = puppiesKittens;
            this.adultDogs = adultDogs;
            this.adultCats = adultCats;
            this.seniorPets = seniorPets;
            this.generalTips = generalTips;
        }

        public String getPuppiesKittens() { return puppiesKittens; }
        public void setPuppiesKittens(String puppiesKittens) { this.puppiesKittens = puppiesKittens; }
        public String getAdultDogs() { return adultDogs; }
        public void setAdultDogs(String adultDogs) { this.adultDogs = adultDogs; }
        public String getAdultCats() { return adultCats; }
        public void setAdultCats(String adultCats) { this.adultCats = adultCats; }
        public String getSeniorPets() { return seniorPets; }
        public void setSeniorPets(String seniorPets) { this.seniorPets = seniorPets; }
        public List<String> getGeneralTips() { return generalTips; }
        public void setGeneralTips(List<String> generalTips) { this.generalTips = generalTips; }
    }
}