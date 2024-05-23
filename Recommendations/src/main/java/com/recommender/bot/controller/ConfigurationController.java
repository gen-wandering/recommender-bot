package com.recommender.bot.controller;

import com.recommender.bot.service.recommendations.AlgorithmsConfig;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigurationController {
    private final AlgorithmsConfig algorithmsConfig;

    public ConfigurationController(AlgorithmsConfig algorithmsConfig) {
        this.algorithmsConfig = algorithmsConfig;
    }

    @GetMapping("/")
    public String getAlgorithmsConfig() {
        return algorithmsConfig.toString();
    }

    @GetMapping("/raw")
    public AlgorithmsConfig getRawAlgorithmsConfig() {
        return algorithmsConfig;
    }

    @PutMapping("/update")
    public AlgorithmsConfig updateAlgorithmsConfig(@RequestBody AlgorithmsConfig newConfig) {
        if (!algorithmsConfig.equals(newConfig)) {
            algorithmsConfig.setNewConfiguration(true);
            algorithmsConfig.merge(newConfig);
        }
        return algorithmsConfig;
    }
}