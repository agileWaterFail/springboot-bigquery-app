package controller

import com.springapp.SpringbootBigqueryAppApplication
import com.springapp.controller.RequestDataSetController
import com.springapp.sal.entity.DataSetEntity
import com.springapp.sal.repository.DataSetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.unitils.dbunit.annotation.DataSet
import org.unitils.dbunit.datasetloadstrategy.impl.CleanInsertLoadStrategy
import spock.lang.Shared
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author davidgiametta
 * @since 4/7/18
 */

@DataSet(
        value = ['test/data/RequestDataSetControllerSpecData.xml'],
        loadStrategy = CleanInsertLoadStrategy
)
@SpringBootTest(
        classes = [SpringbootBigqueryAppApplication]

)
 class RequestDataSetControllerSpec extends Specification{

    @Autowired
    private WebApplicationContext context
    @Autowired
    DataSetRepository repository;

    private MockMvc mockMvc
    @Shared
    private UUID requestId = UUID.randomUUID()
    @Shared
    private DataSetEntity dataSetEntity = DataSetEntity.builder()
        .id(UUID.randomUUID())
        .searchRequestId(requestId)
        .data('test data')
        .build()

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build()

        //setup data.
        repository.save(dataSetEntity)

    }

    def cleanup() {
        //delete the datas
        repository.delete(dataSetEntity)
    }

    def 'Test our RequestDataSet Controller' () {
        given: 'given our requestId'

        when: 'we call our controller'
        def response = mockMvc
                .perform(get(RequestDataSetController.REQUEST_ID_INPUT + requestId))

        then: 'We should see a response of 200'
        response.andExpect(status().isOk())

        def body = response.andReturn()

        then: 'The response should not be null'
        body != null

    }

}