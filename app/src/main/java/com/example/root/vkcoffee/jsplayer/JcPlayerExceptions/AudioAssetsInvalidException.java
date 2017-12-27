package com.example.root.vkcoffee.jsplayer.JcPlayerExceptions;

/**
 * Created by root on 24.12.17.
 */

public class AudioAssetsInvalidException extends Exception {
    public AudioAssetsInvalidException(String path) {
        super("The file name is not a valid Assets file: " + path);
    }
}