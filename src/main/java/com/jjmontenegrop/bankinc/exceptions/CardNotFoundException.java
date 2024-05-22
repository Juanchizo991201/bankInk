package com.jjmontenegrop.bankinc.exceptions;

public class CardNotFoundException extends RuntimeException{

    public CardNotFoundException(String message) {
        super(message);
    }
}
