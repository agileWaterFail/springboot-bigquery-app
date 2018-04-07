package com.springapp.controller.exception;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
public class SearchYearNumericException extends RuntimeException {
    public SearchYearNumericException(final String message) {
        super(message);
    }
}
