package com.pawdcast.pawdcast.application.service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawdcast.pawdcast.application.model.TrainingTip;

import jakarta.annotation.PostConstruct;

@Service
public class TrainingTipService {

    private List<TrainingTip> tips;

    @PostConstruct
    public void loadTips() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/training-tips.json");
            tips = mapper.readValue(inputStream, new TypeReference<List<TrainingTip>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load training tips JSON", e);
        }
    }

    public List<TrainingTip> getAllTips() {
        return tips;
    }

    public List<TrainingTip> getTipsByCategory(String category) {
        return tips.stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
