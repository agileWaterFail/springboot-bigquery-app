package com.springapp.controller;

import com.springapp.controller.exception.SearchYearNumericException;
import com.springapp.orchestrator.SearchOrchestrator;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.UUID;

/**
 * @author davidgiametta
 * @since 4/5/18
 */
@RestController
public class SearchController {

    private SearchOrchestrator searchOrchestrator;

    /*
     * Request Mapping URLs
     */
    public static final String YEAR             = "year";
    public static final String BASE_URL         = "/search";
    public static final String SEARCH_BY_YEAR   = BASE_URL + "/byYear/";
    public static final String EMPTY_RESULTS    = "Sorry no results matched your request.";
    public static final String NORMAL_RESULT    = "Your dataset is now available here: http://localhost:8080"
            + RequestDataSetController.REQUEST_ID_INPUT;
    public static final String MALFORMED_INPUT  = "Unable to extract year and or convert input to numeral";
    public SearchController(final SearchOrchestrator searchOrchestrator) {
        this.searchOrchestrator = searchOrchestrator;
    }

    /**
     * Controller method for searching by year
     *
     * Will handle malformed input with http status 400 BAD REQUEST
     * Will handle internal exceptions for BigQuery with http status 500 INTERNAL SERVER ERROR
     *
     * Should return a ready to use url to retrieve data found by BigQuery or if no results
     * where found an Empty Results message
     *
     * @param yearMap JSONObject in a Map format
     * @return String
     */
    @RequestMapping(value = SEARCH_BY_YEAR, method = RequestMethod.POST)
    public String searchByYear (@RequestBody final Map<String,Object> yearMap) {
        String message = NORMAL_RESULT;

        // Confirm input map contains year field else throw exception
        if (yearMap.containsKey(YEAR)) {
            final String year = yearMap.get(YEAR).toString();
            UUID requestId;

            // Confirm input is Integer else throw exception
            if (isNumeric(year)) {
                requestId = searchOrchestrator.searchByYear(year);
            } else {
                throw new SearchYearNumericException(MALFORMED_INPUT);
            }

            // If requestId is null tell user results are null else return normal results with request id
            if (requestId == null) {
                message = EMPTY_RESULTS;
            } else {
                message += requestId.toString();
            }
        } else {
            throw new SearchYearNumericException(MALFORMED_INPUT);
        }
        return message;
    }

    private static boolean isNumeric(String str) {
        try {
            Integer d = Integer.parseInt(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
