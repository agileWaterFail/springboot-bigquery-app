package com.springapp.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BigQuery Service - setup google credentials and handle all BigQuery searches
 *
 * @author davidgiametta
 * @since 4/5/18
 */
@Service
public class BigQueryService {

    private final String projectId = "testingyourskill-200218";
    private final String filename = "testingYourSkill-eecb97c7a1e9.json";
    private final String credsPath = "/src/main/java/com/springapp/service/creds";
    private final String workingDirectory = System.getProperty("user.dir") + credsPath;

    /**
     * Standard BigQuery setup used from examples altered to make a query for searching by year against
     * bigquery-public-data:bls.unemployment_cps
     * limited to 10 results because I don't know how Google Billing works
     *
     * @param filterYear year to search for
     * @param jobId contains a Unique UUID used for requesting data from our local db
     * @return List<String> JSON Objects converted to string and added to a List
     * @throws Exception
     */
    public List<String> searchByYear(final String filterYear, final JobId jobId) throws Exception {
        final BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId(projectId).setCredentials(getCreds()).build().getService();

        final QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                        "SELECT * " +
                                "FROM [bigquery-public-data:bls.unemployment_cps] cps " +
                                "WHERE cps.year =  " + filterYear +
                                "LIMIT 10")
                        .setUseLegacySql(true)
                        .build();

        // Create a job ID so that we can safely retry.
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        queryJob = queryJob.waitFor();

        // Check for errors
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        // Get the results.
        //final QueryResponse response = bigquery.getQueryResults(jobId);

        final TableResult result = queryJob.getQueryResults();

        final List<String> fieldList = new ArrayList<>();

        fieldList.add("series_id");
        fieldList.add("year");
        fieldList.add("period");
        fieldList.add("value");
        fieldList.add("footnote_codes");
        fieldList.add("date");
        fieldList.add("series_title");

        return tableResultToJSONStringListConverter(result, fieldList);
    }

    private GoogleCredentials getCreds() throws IOException{
        // Load credentials from JSON key file. If you can't set the GOOGLE_APPLICATION_CREDENTIALS
        // environment variable, you can explicitly load the credentials file to construct the
        // credentials.
        GoogleCredentials credentials;

        final File credentialsPath = new File(workingDirectory, filename);
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        }
        return credentials;
    }

    private List<String> tableResultToJSONStringListConverter(TableResult result, List<String> fields) {
        final List<String> queryList = new ArrayList<>();

        for (FieldValueList row : result.iterateAll()) {
            final JSONObject jsonObject = new JSONObject();

            fields.forEach(field -> jsonObject.put(field, row.get(field).getStringValue()));

            queryList.add(jsonObject.toString());
        }

        return queryList;
    }
}
