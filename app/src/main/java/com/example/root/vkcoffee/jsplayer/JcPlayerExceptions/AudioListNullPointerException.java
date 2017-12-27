package com.example.root.vkcoffee.jsplayer.JcPlayerExceptions;

/**
 * Created by root on 24.12.17.
 */

public class AudioListNullPointerException extends NullPointerException {
    public AudioListNullPointerException() {
        super("The playlist is empty or null");
    }
}