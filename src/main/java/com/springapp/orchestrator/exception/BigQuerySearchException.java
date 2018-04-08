package com.springapp.orchestrator.exception;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
public class BigQuerySearchException extends RuntimeException {

    public BigQuerySearchException(final String message) {
        super(message);
    }

    public BigQuerySearchException(final String message, final Exception exception) {
        super(message, exception);
    }
}
