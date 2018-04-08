package com.springapp.controller;

import com.springapp.controller.exception.RequestDataSetBadRequestException;
import com.springapp.entity.DataSetEntity;
import com.springapp.orchestrator.RequestDataSetOrchestrator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    /**
     * Controller method for getting stored data by RequestId
     *
     * Will handle malformed request id with http status 400 BAD REQUEST
     *
     * @param requestId
     * @return List<DataSetEntity> {@link DataSetEntity}
     */
    @RequestMapping(value = GET_DATA_SET, method = RequestMethod.GET)
    public List<DataSetEntity> getDataSet (@RequestParam(name = "requestId") final String requestId) {
        List<DataSetEntity> dataSetEntityList = new ArrayList<>();

        // Confirm input is UUID else throw exception
        if (isUUID(requestId)) {
            dataSetEntityList = requestDataSetOrchestrator.getDataSet(UUID.fromString(requestId));
        } else {
            throw new RequestDataSetBadRequestException("Your request id is not in the proper format (UUID).");
        }

        return dataSetEntityList;

    }

    private static boolean isUUID(String str) {
        try {
            UUID d = UUID.fromString(str);
        } catch(IllegalArgumentException ex) {
            return false;
        }
        return true;
    }
}
