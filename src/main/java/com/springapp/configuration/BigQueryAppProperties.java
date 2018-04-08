package com.springapp.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author davidgiametta
 * @since 4/7/18
 */
@Data
@ConfigurationProperties(prefix = "com.springapp")
public class BigQueryAppProperties {
    private String credentialsPath = "/resources/creds/";
    private String credientalsName = "testingYourSkill-eecb97c7a1e9.json";
    private String projectId = "testingyourskill-200218";

}
