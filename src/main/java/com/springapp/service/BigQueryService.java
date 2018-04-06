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
 * @author davidgiametta
 * @since 4/5/18
 */
@Service
public class BigQueryService {

    private GoogleCredentials getCreds() throws IOException{
        // Load credentials from JSON key file. If you can't set the GOOGLE_APPLICATION_CREDENTIALS
        // environment variable, you can explicitly load the credentials file to construct the
        // credentials.
        GoogleCredentials credentials;
        final String filename = "testingYourSkill-eecb97c7a1e9.json";
        final String workingDirectory = System.getProperty("user.dir") + "/src/main/java/com/springapp/service/creds";

        final File credentialsPath = new File(workingDirectory, filename);
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        }
        return credentials;
    }

    public List<String> searchByYear(final String filterYear, final JobId jobId) throws Exception {
        final BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("testingyourskill-200218").setCredentials(getCreds()).build().getService();

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

        final List<String> queryList = new ArrayList<>();

        for (FieldValueList row : result.iterateAll()) {
            String series_id = row.get("series_id").getStringValue();
            String year = row.get("year").getStringValue();
            String period = row.get("period").getStringValue();
            String value = row.get("value").getStringValue();
            String footnote_codes = row.get("footnote_codes").getStringValue();
            String date = row.get("date").getStringValue();
            String series_title = row.get("series_title").getStringValue();

            final JSONObject jsonObject = new JSONObject();

            jsonObject.put("series_id", series_id);
            jsonObject.put("year", year);
            jsonObject.put("period", period);
            jsonObject.put("value", value);
            jsonObject.put("footnote_codes", footnote_codes);
            jsonObject.put("date", date);
            jsonObject.put("series_title", series_title);

            queryList.add(jsonObject.toString());
        }

        return queryList;
    }
}
