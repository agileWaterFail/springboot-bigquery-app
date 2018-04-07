package com.springapp.controller.exception;

/**
 * @author davidgiametta
 * @since 4/7/18
 */
public class RequestDataSetBadRequestException extends RuntimeException{
    public RequestDataSetBadRequestException(final String message) {
        super(message);
    }
}
