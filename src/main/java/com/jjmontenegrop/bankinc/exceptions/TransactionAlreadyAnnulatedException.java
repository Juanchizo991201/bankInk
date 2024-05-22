package com.jjmontenegrop.bankinc.exceptions;

public class TransactionAlreadyAnnulatedException extends RuntimeException{

                public TransactionAlreadyAnnulatedException(String message) {
                    super(message);
                }
}
