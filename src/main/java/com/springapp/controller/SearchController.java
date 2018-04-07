package com.springapp.controller;

import com.springapp.controller.exception.SearchYearNumericException;
import com.springapp.orchestrator.SearchOrchestrator;
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
        final String year = yearMap.get(YEAR).toString();
        String message = "Your dataset is now available here: http://localhost:8080" +
                RequestDataSetController.REQUEST_ID_INPUT;
        UUID requestId;

        if (isNumeric(year)) {
                requestId = searchOrchestrator.searchByYear(year);
        } else {
            throw new SearchYearNumericException("Unable to convert input to numeral");
        }

        if (requestId != null) {
            message += requestId.toString();
        } else {
            message = EMPTY_RESULTS;
        }

        return message;
    }

    private static boolean isNumeric(String str)
    {
        try
        {
            Integer d = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
