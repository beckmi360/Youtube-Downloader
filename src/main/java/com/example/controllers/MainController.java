package com.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;

public class MainController {
    @FXML private ImageView thumbnailView;
    @FXML private TextField urlTextField;
    @FXML private ProgressBar progressBar;
    
    private DownloadController downloadController;
    private ThumbnailController thumbnailController;
    private ProgressBarController progressBarController;
    
    @FXML
    public void initialize() {
        progressBarController = new ProgressBarController(progressBar);
        downloadController = new DownloadController(urlTextField, progressBarController);
        thumbnailController = new ThumbnailController(thumbnailView);
    }
    
    @FXML
    public void downloadVideo() {
        String url = urlTextField.getText();
        if (url != null && !url.trim().isEmpty()) {
            thumbnailController.updateThumbnail(url);
            downloadController.startDownload(url);
        }
    }
}