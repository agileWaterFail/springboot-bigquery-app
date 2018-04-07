package com.springapp.orchestrator;

import com.springapp.entity.DataSetEntity;
import com.springapp.repository.DataSetRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
@Component
public class RequestDataSetOrchestrator {

    private DataSetRepository repository;

    public RequestDataSetOrchestrator(final DataSetRepository repository) {
        this.repository = repository;
    }

    public List<DataSetEntity> getDataSet(final UUID requestId) {
        return repository.findBySearchRequestId(requestId);
    }
}
