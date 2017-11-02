package de.jeha.rgwadm4j.config;

/**
 * @author jenshadlich@googlemail.com
 */
public class RgwAdmConfig {

    private final String accessKey;
    private final String secretKey;
    private final String endpoint;

    public RgwAdmConfig(String accessKey, String secretKey, String endpoint) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

}
