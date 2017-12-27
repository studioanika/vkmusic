package com.example.root.vkcoffee.jsplayer.JcPlayerExceptions;

/**
 * Created by root on 24.12.17.
 */

public class AudioFilePathInvalidException extends Exception {
    public AudioFilePathInvalidException(String url) {
        super("The file path is not a valid path: " + url +
                "\n" +
                "Have you add File Access Permission?");
    }
}