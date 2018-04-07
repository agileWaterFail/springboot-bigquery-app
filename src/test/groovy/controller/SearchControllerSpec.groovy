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
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(
        classes = [SpringbootBigqueryAppApplication])
class SearchControllerSpec extends Specification {

    @Autowired
    private WebApplicationContext context

    private MockMvc mockMvc

    private ObjectMapper mapper

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build()

        this.mapper = new ObjectMapper()
        this.mapper.registerModule(new Jdk8Module())
        this.mapper.registerModule(new JavaTimeModule())
        this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    def 'Test our SearchController' () {
        given: 'given data (year)'
        Map<String,Object> year = new HashMap<>()
        year.put("year", "1987")
        def jsonValue = mapper.writeValueAsString(year)

        when: 'we call our controller'
        def response = mockMvc
                .perform(post(SearchController.SEARCH_BY_YEAR )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonValue))
        then: 'We should see a response of 200'
        response.andExpect(status().isOk())

        def body = response.andReturn()

        then: 'The response should not be null'
        body != null

        then: 'the response should be a string'
        assert body.response.contentAsString.contains("Your dataset is now available here: http://localhost:8080" + RequestDataSetController.REQUEST_ID_INPUT)

    }

    def 'Test our SearchController and get no results' () {
        given: 'given data (year)'
        Map<String,Object> year = new HashMap<>()
        year.put("year", "198735")
        def jsonValue = mapper.writeValueAsString(year)

        when: 'we call our controller'
        def response = mockMvc
                .perform(post(SearchController.SEARCH_BY_YEAR )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonValue))
        then: 'We should see a response of 200'
        response.andExpect(status().isOk())

        def body = response.andReturn()

        then: 'The response should not be null'
        body != null

        then: 'the response should be a string'
        assert body.response.contentAsString.equals(SearchController.EMPTY_RESULTS)

    }

    def 'Test our SearchController with a bad request' () {
        given: 'given bad data (year)'
        Map<String,Object> year = new HashMap<>()
        year.put("year", "ASDF")
        def jsonValue = mapper.writeValueAsString(year)

        when: 'we call our controller'
        def response = mockMvc
                .perform(post(SearchController.SEARCH_BY_YEAR )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonValue))
        then: 'We should see a response of 400'
        response.andExpect(status().isBadRequest())

    }

}