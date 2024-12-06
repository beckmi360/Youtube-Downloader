package com.example.controllers;

import com.example.services.ThumbnailService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

public class ThumbnailController {
    private final ImageView thumbnailView;
    private final ThumbnailService thumbnailService;
    private String currentThumbnailPath;
    
    public ThumbnailController(ImageView thumbnailView) {
        this.thumbnailView = thumbnailView;
        this.thumbnailService = new ThumbnailService();
    }
    
    public void updateThumbnail(String url) {
        new Thread(() -> {
            try {
                // Delete previous thumbnail if it exists
                if (currentThumbnailPath != null) {
                    new File(currentThumbnailPath).delete();
                }
                
                // Download new thumbnail
                currentThumbnailPath = thumbnailService.downloadThumbnail(url);
                
                if (currentThumbnailPath != null) {
                    File thumbnailFile = new File(currentThumbnailPath);
                    Image thumbnail = new Image(thumbnailFile.toURI().toString());
                    
                    javafx.application.Platform.runLater(() -> {
                        thumbnailView.setImage(thumbnail);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}