package com.example.root.vkcoffee.jsplayer.JcPlayerExceptions;

/**
 * Created by root on 24.12.17.
 */

public class AudioRawInvalidException extends Exception {
    public AudioRawInvalidException(String rawId) {
        super("Not a valid raw file id: " + rawId);
    }
}
