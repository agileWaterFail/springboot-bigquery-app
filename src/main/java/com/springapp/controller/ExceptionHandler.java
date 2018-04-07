package com.springapp.controller;

import com.springapp.orchestrator.exception.SearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    private static final String HANDLED_ACTION_FIELD_LOG_TEMPLATE = "handled_action={}";

    @org.springframework.web.bind.annotation.ExceptionHandler({SearchException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleGenericValidationException(final RuntimeException exception) {
        return logExceptionAndGetMessage("big_query_failure_clause", exception);
    }

    private String logExceptionAndGetMessage(final String actionName, final Exception exception) {
        log.warn(HANDLED_ACTION_FIELD_LOG_TEMPLATE, actionName, exception);
        return exception.getMessage();
    }
}
