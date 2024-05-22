package com.jjmontenegrop.bankinc.exceptions;

public class CardNotAvailableException extends RuntimeException{

        public CardNotAvailableException(String message) {
            super(message);
        }
}
