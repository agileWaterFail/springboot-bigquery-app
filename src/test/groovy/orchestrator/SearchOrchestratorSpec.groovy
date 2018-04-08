package orchestrator

import com.google.cloud.bigquery.JobId
import com.springapp.orchestrator.SearchOrchestrator
import com.springapp.sal.repository.DataSetRepository
import com.springapp.service.BigQueryService
import spock.lang.Specification
import spock.lang.Subject

class SearchOrchestratorSpec extends Specification {

    @Subject
    private SearchOrchestrator tested

    private BigQueryService service;
    private DataSetRepository repository;
    private def data = [];

    void setup() {
        data.add('testing your attention please')

        service = Mock(BigQueryService)
        repository = Mock(DataSetRepository)

        tested = new SearchOrchestrator(service, repository)
    }

    def 'Orchestrator test'(){
        given: 'Our our input data (year) for searching'
        def year = '1987'

        when: 'we make the call to searchByYear'
        def result = tested.searchByYear(year)

        then: 'we expect a call to the service'
        1 * service.searchByYear(year, _ as JobId) >> data

        then: 'we expect a call to save our resulting entity'
        1 * repository.saveAll(_)

        then: 'result should be a unique UUID'
        assert result instanceof Optional<UUID>
    }

    def 'Orchestrator test with no results'(){
        given: 'Our our input data (year) for searching'
        def year = '1987324'
        data = []

        when: 'we make the call to searchByYear'
        def result = tested.searchByYear(year)

        then: 'we expect a call to the service with no results returned'
        1 * service.searchByYear(year, _ as JobId) >> data

        then: 'we expect no call to save our resulting entity'
        0 * repository.saveAll(_)

        then: 'result should be null'
        assert result == Optional.empty()
    }

}