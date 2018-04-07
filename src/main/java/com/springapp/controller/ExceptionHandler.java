package com.springapp.controller;

import com.springapp.controller.exception.RequestDataSetBadRequestException;
import com.springapp.controller.exception.SearchYearNumericException;
import com.springapp.orchestrator.exception.BigQuerySearchException;
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

    @org.springframework.web.bind.annotation.ExceptionHandler({BigQuerySearchException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleSearchException(final RuntimeException exception) {
        return logExceptionAndGetMessage("big_query_failure_clause", exception);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({SearchYearNumericException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleSearchYearNumericExceptionn(final RuntimeException exception) {
        return logExceptionAndGetMessage("input_conversion_failure_clause", exception);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({RequestDataSetBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleRequestDataSetBadRequestException(final RuntimeException exception) {
        return logExceptionAndGetMessage("input_conversion_failure_clause_2", exception);
    }

    private String logExceptionAndGetMessage(final String actionName, final Exception exception) {
        log.warn(HANDLED_ACTION_FIELD_LOG_TEMPLATE, actionName, exception);
        return exception.getMessage();
    }
}
