package com.pawdcast.pawdcast.application.model;

import java.util.List;

public class PetDietTips {
    private List<GeneralTipCategory> generalTips;
    private List<SpecialConsideration> specialConsiderations;
    private List<TreatGuideline> treatGuidelines;
    private List<String> emergencySigns;

    // Constructors
    public PetDietTips() {}

    public PetDietTips(List<GeneralTipCategory> generalTips, List<SpecialConsideration> specialConsiderations, 
                      List<TreatGuideline> treatGuidelines, List<String> emergencySigns) {
        this.generalTips = generalTips;
        this.specialConsiderations = specialConsiderations;
        this.treatGuidelines = treatGuidelines;
        this.emergencySigns = emergencySigns;
    }

    // Getters and Setters
    public List<GeneralTipCategory> getGeneralTips() { return generalTips; }
    public void setGeneralTips(List<GeneralTipCategory> generalTips) { this.generalTips = generalTips; }

    public List<SpecialConsideration> getSpecialConsiderations() { return specialConsiderations; }
    public void setSpecialConsiderations(List<SpecialConsideration> specialConsiderations) { this.specialConsiderations = specialConsiderations; }

    public List<TreatGuideline> getTreatGuidelines() { return treatGuidelines; }
    public void setTreatGuidelines(List<TreatGuideline> treatGuidelines) { this.treatGuidelines = treatGuidelines; }

    public List<String> getEmergencySigns() { return emergencySigns; }
    public void setEmergencySigns(List<String> emergencySigns) { this.emergencySigns = emergencySigns; }

    // Inner classes
    public static class GeneralTipCategory {
        private String category;
        private List<String> tips;

        public GeneralTipCategory() {}

        public GeneralTipCategory(String category, List<String> tips) {
            this.category = category;
            this.tips = tips;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getTips() { return tips; }
        public void setTips(List<String> tips) { this.tips = tips; }
    }

    public static class SpecialConsideration {
        private String category;
        private List<String> tips;

        public SpecialConsideration() {}

        public SpecialConsideration(String category, List<String> tips) {
            this.category = category;
            this.tips = tips;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getTips() { return tips; }
        public void setTips(List<String> tips) { this.tips = tips; }
    }

    public static class TreatGuideline {
        private String tip;
        private String explanation;

        public TreatGuideline() {}

        public TreatGuideline(String tip, String explanation) {
            this.tip = tip;
            this.explanation = explanation;
        }

        public String getTip() { return tip; }
        public void setTip(String tip) { this.tip = tip; }
        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }
}