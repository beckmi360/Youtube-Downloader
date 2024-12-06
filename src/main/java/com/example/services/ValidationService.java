package com.example.services;

import java.io.IOException;

public class ValidationService {
    public boolean isValidYoutubeUrl(String url) {
        return url != null && url.matches("^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+");
    }

    public boolean isYtDlpInstalled() {
        try {
            ProcessBuilder checkYtDlp = new ProcessBuilder("yt-dlp", "--version");
            Process process = checkYtDlp.start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}