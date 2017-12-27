package com.example.root.vkcoffee.jsplayer.JcPlayerExceptions;

/**
 * Created by root on 24.12.17.
 */

public class AudioUrlInvalidException extends IllegalStateException {
    public AudioUrlInvalidException(String url) {
        super("The url does not appear valid: " + url);
    }
}