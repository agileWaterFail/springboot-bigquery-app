package com.springapp.service.utility;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author davidgiametta
 * @since 4/7/18
 */
@UtilityClass
public class GoogleCredentialsUtility {

    public static GoogleCredentials getCreds(final String workingDirectory, final String filename) throws IOException {
        // Load credentials from JSON key file. If you can't set the GOOGLE_APPLICATION_CREDENTIALS
        // environment variable, you can explicitly load the credentials file to construct the
        // credentials.
        GoogleCredentials credentials;
        final String credsPath = File.separator + "src" + File.separator + "main" + File.separator;

        final File credentialsPath = new File(System.getProperty("user.dir") + credsPath + workingDirectory, filename);
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        }
        return credentials;
    }
}
