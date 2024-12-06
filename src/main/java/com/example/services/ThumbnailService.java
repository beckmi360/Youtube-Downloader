package com.example.services;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;

public class ThumbnailService {
    private static final String TEMP_DIR = "thumbnails";

    public String downloadThumbnail(String videoUrl) throws IOException, InterruptedException {
        // Create thumbnails directory if it doesn't exist
        new File(TEMP_DIR).mkdirs();
    
        ProcessBuilder processBuilder = new ProcessBuilder(
            "yt-dlp",
            "--quiet",
            "--write-thumbnail",
            "--convert-thumbnails", "jpg",  // Convert thumbnails to JPG
            "--skip-download",
            "--output", TEMP_DIR + "/%(id)s.%(ext)s",
            videoUrl
        );
    
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            throw new IOException("Failed to download thumbnail. Exit code: " + exitCode);
        }
    
        String thumbnailPath = findThumbnailFile();
        if (thumbnailPath == null) {
            throw new IOException("Thumbnail file not found after download");
        }
        
        return thumbnailPath;
    }

    private String findThumbnailFile() {
        File dir = new File(TEMP_DIR);
        File[] thumbnailFiles = dir.listFiles((d, name) -> 
            name.endsWith(".jpg")  // Only look for JPG files
        );
    
        if (thumbnailFiles == null || thumbnailFiles.length == 0) {
            return null;
        }
    
        // Get the most recently modified file
        File latest = thumbnailFiles[0];
        for (File file : thumbnailFiles) {
            if (file.lastModified() > latest.lastModified()) {
                latest = file;
            }
        }
        
        return latest.getAbsolutePath();
    }

    public Image loadImage(String thumbnailPath) {
        File file = new File(thumbnailPath);
        if (!file.exists()) {
            throw new RuntimeException("Thumbnail file not found: " + thumbnailPath);
        }
        
        try {
            String uri = file.toURI().toString();
            System.out.println("Loading image from URI: " + uri);
            // Let the ImageView handle the sizing
            Image image = new Image(uri);
            
            if (image.isError()) {
                throw new RuntimeException("Failed to load image: " + image.getException());
            }
            
            return image;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage(), e);
        }
    }

    public void cleanup(String thumbnailPath) {
        if (thumbnailPath != null) {
            File file = new File(thumbnailPath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    System.err.println("Failed to delete thumbnail: " + thumbnailPath);
                }
            }
        }
    }
}