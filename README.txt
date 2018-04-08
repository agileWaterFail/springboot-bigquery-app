
APPLICATION: springboot-bigquery-app
AUTHOR: David Giametta
DATE: Apr 5-8 2018
Build tool: Apache Maven 3.5.0
IDE: Intellij
DB: POSTGRES
    PORT: 5432

Note: Please reference the ApplicationRequirements.txt to see the requirements that drove development
of this application

Table of Contents:
1) To Run
2) Exposed endpoints and their usage
3) Enhancements
4) Tests
5) Query Limit

1) To Run
    Missing from the application are google certs you'll need - google cloud account
    credentials json file

    Update credentials.properties to point at your json file and specify your project id

2) Exposed endpoints and their usage
    POST:
        http://localhost:8080/search/byYear/
            headers:
                Key: Content-Type
                Value: application/json

            body:
                example:
                {
                  "year" : "1967"
                }
    GET:
        http://localhost:8080/requestId/
            params:
                Key: requestId
                Value: UUID
    USAGE:
        Using POSTMAN give the post endpoint a year and send then click the resulting url and send

3) Enhancements
    1) Stop passing around the entity and convert it to an internal model for use throughout the app using a
    ConversionService bean and a custom converter that trims unnecessary fields
    2) Add more tests following a proper BDD Pyramid
    3) Better Exception Handling in BigQueryService
    4) Learn more React and add a front end ui

4) Tests
    Testing is done using groovy spock tests and are made available for viewing after a maven build in
    /build/spock-reports/index.html

5) Query Limit
    Currently the query is limited to 10 - read code comments in BigQueryService.java