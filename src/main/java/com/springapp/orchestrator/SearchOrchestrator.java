package com.springapp.orchestrator;

import com.google.cloud.bigquery.JobId;
import com.springapp.entity.DataSetEntity;
import com.springapp.repository.DataSetRepository;
import com.springapp.service.BigQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
@Component
@Slf4j
public class SearchOrchestrator {

    @Autowired
    private BigQueryService service;

    @Autowired
    private DataSetRepository repository;

    public UUID searchByYear(final String year) {
        final UUID reqeustId = UUID.randomUUID();
        final JobId jobId = JobId.of(reqeustId.toString());
        try {
            final List<String> resultList = service.searchByYear(year, jobId);

            resultList.forEach(result -> {
                final DataSetEntity dataSetEntity = DataSetEntity.builder()
                        .id(UUID.randomUUID())
                        .searchRequestId(reqeustId)
                        .data(result)
                        .build();

                repository.save(dataSetEntity);
            });
        } catch (Exception ex) {
            log.error("AHHHHHHH",ex );
        }

        return reqeustId;
    }
}
