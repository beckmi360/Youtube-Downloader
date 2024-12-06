package com.example.services;

import java.io.IOException;


public class DownloadService {
    public Process startDownload(String videoUrl) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
            "yt-dlp",
            "--newline",
            "--console-title",
            videoUrl
        );
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    public double parseProgress(String line) {
        String[] parts = line.split("\\s+");
        for (String part : parts) {
            if (part.endsWith("%")) {
                try {
                    String percentStr = part.substring(0, part.length() - 1);
                    return Double.parseDouble(percentStr) / 100.0;
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse progress: " + part);
                }
            }
        }
        return -1;
    }
}