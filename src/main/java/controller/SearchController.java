package controller;

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


    /*
     * Request Mapping URLs
     */

    public static final String BASE_URL         = "/search";

    public static final String SEARCH_BY_YEAR   = BASE_URL + "/byYear/";

    @RequestMapping(value = SEARCH_BY_YEAR, method = RequestMethod.POST)
    public HttpStatus searchByYear (@RequestBody final String year) {
        System.out.println(year);
        return HttpStatus.I_AM_A_TEAPOT;
    }

}
