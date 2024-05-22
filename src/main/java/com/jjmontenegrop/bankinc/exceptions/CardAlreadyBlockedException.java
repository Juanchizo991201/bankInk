package com.jjmontenegrop.bankinc.exceptions;

public class CardAlreadyBlockedException extends RuntimeException{

        public CardAlreadyBlockedException(String message) {
            super(message);
        }
}
