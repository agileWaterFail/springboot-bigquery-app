package controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.springapp.SpringbootBigqueryAppApplication
import com.springapp.controller.RequestDataSetController
import com.springapp.controller.SearchController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.unitils.dbunit.annotation.DataSet
import org.unitils.dbunit.datasetloadstrategy.impl.CleanInsertLoadStrategy
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
        classes = [SpringbootBigqueryAppApplication])
 class RequestDataSetControllerSpec extends Specification{
    def requestId = 'a4a76bf5-4a0e-4031-a63a-103a419dac8d'

    @Autowired
    private WebApplicationContext context

    private MockMvc mockMvc

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build()

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