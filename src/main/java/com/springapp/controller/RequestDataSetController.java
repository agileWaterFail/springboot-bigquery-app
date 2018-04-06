package com.springapp.controller;

import com.springapp.entity.DataSetEntity;
import com.springapp.orchestrator.RequestDataSetOrchestrator;
import com.springapp.orchestrator.SearchOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


/**
 * @author davidgiametta
 * @since 4/6/18
 */
@RestController
public class RequestDataSetController {

    private RequestDataSetOrchestrator requestDataSetOrchestrator;
    /*
     * Request Mapping URLs
    */
    public static final String GET_DATA_SET = "/requestId";
    public static final String REQUEST_ID_INPUT = GET_DATA_SET + "/?requestId=";

    public RequestDataSetController(final RequestDataSetOrchestrator requestDataSetOrchestrator) {
        this.requestDataSetOrchestrator = requestDataSetOrchestrator;
    }

    @RequestMapping(value = GET_DATA_SET, method = RequestMethod.GET)
    public List<DataSetEntity> getDataSet (@RequestParam(name = "requestId") final String requestId) {
        return requestDataSetOrchestrator.getDataSet(UUID.fromString(requestId));

    }
}
