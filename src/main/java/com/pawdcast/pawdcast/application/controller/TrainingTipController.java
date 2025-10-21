package com.pawdcast.pawdcast.application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pawdcast.pawdcast.application.model.TrainingTip;
import com.pawdcast.pawdcast.application.service.TrainingTipService;

@RestController
@RequestMapping("/api/training-tips")
@CrossOrigin(origins = "*") 
public class TrainingTipController {

    private final TrainingTipService trainingTipService;

    public TrainingTipController(TrainingTipService trainingTipService) {
        this.trainingTipService = trainingTipService;
    }

    // GET all tips
    @GetMapping
    public List<TrainingTip> getAllTips() {
        return trainingTipService.getAllTips();
    }

    // GET tips by category
    @GetMapping("/{category}")
    public List<TrainingTip> getTipsByCategory(@PathVariable String category) {
        return trainingTipService.getTipsByCategory(category);
    }
}
