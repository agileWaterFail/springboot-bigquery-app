package com.springapp.orchestrator;

import com.google.cloud.bigquery.JobId;
import com.springapp.entity.DataSetEntity;
import com.springapp.orchestrator.exception.SearchException;
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

    private BigQueryService service;

    private DataSetRepository repository;

    public SearchOrchestrator(final BigQueryService service, final DataSetRepository repository) {
        this.service = service;
        this.repository = repository;
    }

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
            String message = "Failure to searchByYear";
            throw new SearchException(message,ex);
        }

        return reqeustId;
    }
}
