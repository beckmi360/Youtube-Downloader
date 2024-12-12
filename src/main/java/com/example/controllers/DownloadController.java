package com.example.controllers;

import com.example.services.DownloadService;
import com.example.services.ValidationService;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DownloadController {
    private final ProgressBarController progressBarController;
    private final DownloadService downloadService;
    private final ValidationService validationService;
    
    public DownloadController(TextField urlTextField, ProgressBarController progressBarController) {
        this.progressBarController = progressBarController;
        this.downloadService = new DownloadService();
        this.validationService = new ValidationService();
    }
    
    public void startDownload(String url) {
        if (!validationService.isValidYoutubeUrl(url)) {
            // Handle invalid URL
            return;
        }
        
        if (!validationService.isYtDlpInstalled()) {
            // Handle missing yt-dlp
            return;
        }
        
        new Thread(() -> {
            try {
                Process process = downloadService.startDownload(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                
                while ((line = reader.readLine()) != null) {
                    final String currentLine = line;
                    double progress = downloadService.parseProgress(currentLine);
                    if (progress >= 0) {
                        Platform.runLater(() -> progressBarController.updateProgress(progress));
                    }
                }
                
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    Platform.runLater(() -> progressBarController.resetProgress());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> progressBarController.resetProgress());
            }
        }).start();
    }
}