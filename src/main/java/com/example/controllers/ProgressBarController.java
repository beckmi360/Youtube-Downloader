package com.example.controllers;

import javafx.scene.control.ProgressBar;

public class ProgressBarController {
    private final ProgressBar progressBar;
    
    public ProgressBarController(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    
    public void updateProgress(double progress) {
        progressBar.setProgress(progress);
    }
    
    public void resetProgress() {
        progressBar.setProgress(0);
    }
}