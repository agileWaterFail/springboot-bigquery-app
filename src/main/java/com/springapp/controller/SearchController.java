package com.springapp.controller;

import com.springapp.orchestrator.SearchOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public static final String BASE_URL         = "/search";
    public static final String SEARCH_BY_YEAR   = BASE_URL + "/byYear/";

    public SearchController(final SearchOrchestrator searchOrchestrator) {
        this.searchOrchestrator = searchOrchestrator;
    }

    @RequestMapping(value = SEARCH_BY_YEAR, method = RequestMethod.POST)
    public String searchByYear (@RequestBody final String year) {
        String message =
                "Your dataset is available for download here: http://localhost:8080" +
                RequestDataSetController.REQUEST_ID_INPUT +
                searchOrchestrator.searchByYear(year).toString();

        return message;
    }

}
