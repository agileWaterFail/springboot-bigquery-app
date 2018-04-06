package orchestrator

import com.springapp.entity.DataSetEntity
import com.springapp.orchestrator.RequestDataSetOrchestrator
import com.springapp.repository.DataSetRepository
import spock.lang.Specification
import spock.lang.Subject


class RequestDataSetOrchestratorSpec extends Specification {

    @Subject
    private RequestDataSetOrchestrator tested

    private DataSetRepository repository;

    List<DataSetEntity> data = new ArrayList<>();
    void setup() {
        data.add(DataSetEntity.builder()
            .id(UUID.randomUUID())
            .data('testing your attention please')
            .searchRequestId(UUID.randomUUID())
            .build()
        )

        repository = Mock(DataSetRepository)

        tested = new RequestDataSetOrchestrator(repository)
    }

    def 'Orchestrator test'(){
        given: 'Our our input data (year) for searching'

        when: 'we make the call to searchByYear'
        def result = tested.getDataSet(UUID.randomUUID())

        then: 'we expect a call to request our dataSets'
        1 * repository.findBySearchRequestId(_ as UUID) >> data

        then: 'results should contain what we expect'
        assert result == data
    }
}