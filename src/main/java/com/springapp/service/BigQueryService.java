package com.springapp.service;

import com.google.api.services.bigquery.model.JsonObject;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.TableResult;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author davidgiametta
 * @since 4/5/18
 */
@Service
public class BigQueryService {
//    private final BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

    public JSONObject searchByYear(final String year, final JobId jobId) {
        JSONObject jsonString = new JSONObject()
                .put("year", year)
                .put("JSON2", "Hello my World!")
                .put("JSON3", new JSONObject()
                        .put("key1", "value1"));
//        QueryJobConfiguration queryConfig =
//                QueryJobConfiguration.newBuilder(String.format(
//                        "SELECT "
//                                + "TO_JSON_STRING(*,true)"
//                                + "FROM `bigquery-public-data:bls.unemployment_cps cps` "
//                                + "WHERE cps.year = %d "
//                                + "ORDER BY cps.value DESC",year))
//                        // Use standard SQL syntax for queries.
//                        // See: https://cloud.google.com/bigquery/sql-reference/
//                        .setUseLegacySql(false)
//                        .build();
//
//// Create a job ID so that we can safely retry.
//        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
//
//// Wait for the query to complete.
//        //queryJob = queryJob.waitFor();
//
//// Check for errors
//        if (queryJob == null) {
//            throw new RuntimeException("Job no longer exists");
//        } else if (queryJob.getStatus().getError() != null) {
//            // You can also look at queryJob.getStatus().getExecutionErrors() for all
//            // errors, not just the latest one.
//            throw new RuntimeException(queryJob.getStatus().getError().toString());
//        }
//        // Get the results.
//        QueryResponse response = bigquery.getQueryResults(jobId);
//
//        TableResult result = queryJob.getQueryResults();

        return jsonString;
    }
}
