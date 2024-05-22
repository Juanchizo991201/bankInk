package com.jjmontenegrop.bankinc.exceptions;

public class CardAlreadyActivatedException extends RuntimeException{

        public CardAlreadyActivatedException(String message) {
            super(message);
        }
}
