package com.springapp.orchestrator.exception;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
public class SearchException extends RuntimeException {
    public SearchException(final String message, final Exception exception) {
        super(message, exception);
    }
}
