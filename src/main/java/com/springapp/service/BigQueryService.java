package com.springapp.service;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import com.springapp.configuration.BigQueryAppProperties;
import com.springapp.orchestrator.exception.BigQuerySearchException;
import com.springapp.service.utility.GoogleCredentialsUtility;
import org.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * BigQuery Service - setup google credentials and handle all BigQuery searches
 *
 * @author davidgiametta
 * @since 4/5/18
 */
@Service
@EnableConfigurationProperties({BigQueryAppProperties.class})
public class BigQueryService {

    private final BigQueryAppProperties properties;

    public BigQueryService(BigQueryAppProperties properties) {
        this.properties = properties;
    }

    /**
     * Standard BigQuery setup used from examples altered to make a query for searching by year against
     * bigquery-public-data:bls.unemployment_cps
     * limited to 10 results because I don't know how Google Billing works
     *
     * Exception handling could be better
     *
     * @param filterYear year to search for
     * @param jobId      contains a Unique UUID used for requesting data from our local db
     *
     * @return List<String> JSON Objects converted to string and added to a List
     * @throws Exception
     */
    public List<String> searchByYear(final String filterYear, final JobId jobId) throws BigQuerySearchException {
        BigQuery bigquery;

        try {
            bigquery = BigQueryOptions.newBuilder().setProjectId(properties.getProjectId()).setCredentials(GoogleCredentialsUtility.getCreds(properties.getCredentialsPath(), properties.getCredientalsName())).build().getService();
        } catch (Exception ex) {
            throw new BigQuerySearchException("IO Exception thrown - can't find google credintials file", ex);
        }

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
        try {
            queryJob = queryJob.waitFor();

            // Check for errors
            if (queryJob == null) {
                throw new BigQuerySearchException("Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                // You can also look at queryJob.getStatus().getExecutionErrors() for all
                // errors, not just the latest one.
                throw new BigQuerySearchException(queryJob.getStatus().getError().toString());
            }
            // Get the results.
            //final QueryResponse response = bigquery.getQueryResults(jobId);

            final TableResult result = queryJob.getQueryResults();

            final List<String> fieldList = new ArrayList<>();

            //build list of fields in table results expected from query
            fieldList.add("series_id");
            fieldList.add("year");
            fieldList.add("period");
            fieldList.add("value");
            fieldList.add("footnote_codes");
            fieldList.add("date");
            fieldList.add("series_title");

            return tableResultToJSONStringListConverter(result, fieldList);
        } catch (InterruptedException ex) {
            throw new BigQuerySearchException("InterruptedException - query job failed", ex);
        }
    }

    private List<String> tableResultToJSONStringListConverter(TableResult result, List<String> fields) {
        final List<String> queryList = new ArrayList<>();

        // loop throw table results and convert to/add JSONObjects to string list
        // this is slow and could be fixed with intermediate object
        for (FieldValueList row : result.iterateAll()) {
            final JSONObject jsonObject = new JSONObject();

            fields.forEach(field -> jsonObject.put(field, row.get(field).getStringValue()));

            queryList.add(jsonObject.toString());
        }

        return queryList;
    }


}
