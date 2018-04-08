package com.springapp.orchestrator;

import com.google.cloud.bigquery.JobId;
import com.springapp.entity.DataSetEntity;
import com.springapp.orchestrator.exception.BigQuerySearchException;
import com.springapp.repository.DataSetRepository;
import com.springapp.service.BigQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Orchestrate all BigQuery searches and Create/Update calls to the DB
 *
 * @author davidgiametta
 * @since 4/6/18
 */
@Component
@Slf4j
public class SearchOrchestrator {

    private BigQueryService service;

    private DataSetRepository repository;

    public SearchOrchestrator(final BigQueryService service, final DataSetRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    /**
     * Search by year
     *
     * Try to use BigQuery to search by year - Throw internal exception on failure
     * Will handle empty results by passing back null UUID
     * Make saveall call to the repo on successful results from BigQuery
     *
     * @param year the year to search for
     * @return UUID {@link UUID}
     */

    public UUID searchByYear(final String year) {
        final UUID reqeustId = UUID.randomUUID();
        final JobId jobId = JobId.of(reqeustId.toString());
        final List<DataSetEntity> dataSetEntityList = new ArrayList<>();
        List<String> resultList = new ArrayList<>();

        // call our bigquery service
        resultList = service.searchByYear(year, jobId);

        // if result list is empty return null else save down results and return normal requestId
        if (resultList.isEmpty()) {
            return null;
        } else {
            resultList.forEach(result -> {
                final DataSetEntity dataSetEntity = DataSetEntity.builder()
                        .id(UUID.randomUUID())
                        .searchRequestId(reqeustId)
                        .data(result)
                        .build();

                dataSetEntityList.add(dataSetEntity);
            });

            repository.saveAll(dataSetEntityList);
        }
        return reqeustId;
    }
}
