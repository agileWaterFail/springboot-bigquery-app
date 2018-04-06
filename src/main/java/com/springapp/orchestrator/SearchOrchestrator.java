package com.springapp.orchestrator;

import com.google.cloud.bigquery.JobId;
import com.springapp.entity.DataSetEntity;
import com.springapp.repository.DataSetRepository;
import com.springapp.service.BigQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
@Component
public class SearchOrchestrator {

    @Autowired
    private BigQueryService service;

    @Autowired
    private DataSetRepository repository;

    public String searchByYear(final String year) {
        final UUID reqeustId = UUID.randomUUID();
        final JobId jobId = JobId.of(reqeustId.toString());

        final DataSetEntity dataSetEntity = DataSetEntity.builder()
                .id(UUID.randomUUID())
                .searchRequestId(reqeustId)
                .data(service.searchByYear(year, jobId).toString())
                .build();

        repository.save(dataSetEntity);


        return "Your dataset is available for download here";
    }
}
