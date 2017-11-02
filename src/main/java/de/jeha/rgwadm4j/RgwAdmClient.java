package de.jeha.rgwadm4j;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.jeha.rgwadm4j.config.RgwAdmConfig;
import de.jeha.rgwadm4j.model.BucketStats;
import de.jeha.rgwadm4j.utils.AuthorizationUtils;
import de.jeha.rgwadm4j.utils.RFC822Date;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jenshadlich@googlemail.com
 */
public class RgwAdmClient {

    private final RgwAdmConfig rgwAdmConfig;

    public RgwAdmClient(String accessKey, String secretKey, String endpoint) {
        this.rgwAdmConfig = new RgwAdmConfig(accessKey, secretKey, endpoint);
    }

    public List<BucketStats> getBucketStats() throws IOException {
        final CloseableHttpClient httpClient = buildHttpClient();
        final HttpGet request = new HttpGet(rgwAdmConfig.getEndpoint() + "/admin/bucket?format=json&stats=true");
        final Date date = new Date();
        final String dateString = RFC822Date.format(date);
        final String authorization =
                AuthorizationUtils.computeAuthorization(rgwAdmConfig.getAccessKey(), rgwAdmConfig.getSecretKey(), "GET", dateString, "/admin/bucket");

        request.addHeader("Date", dateString);
        request.addHeader("Authorization", authorization);

        final HttpResponse response = httpClient.execute(request);

        System.out.println(response.getStatusLine());

        final List<BucketStats> bucketStats = new ArrayList<>();
        final JsonParser jsonParser = new JsonParser();
        for (JsonElement e : jsonParser.parse(new InputStreamReader(response.getEntity().getContent())).getAsJsonArray()) {
            final JsonObject o = e.getAsJsonObject();
            final JsonObject rgwUsage = o.getAsJsonObject("usage").getAsJsonObject("rgw.main");

            final String bucket = o.get("bucket").getAsString();
            final String owner = o.get("owner").getAsString();
            final long numObject = rgwUsage.get("num_objects").getAsLong();
            final long size = rgwUsage.get("size").getAsLong();

            bucketStats.add(new BucketStats(bucket, owner, numObject, size));
        }

        return bucketStats;
    }

    private CloseableHttpClient buildHttpClient() {
        final RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1_000)
                .setSocketTimeout(1_000)
                .setRedirectsEnabled(false)
                .setContentCompressionEnabled(false)
                .build();

        return HttpClientBuilder.create()
                .setConnectionManager(new BasicHttpClientConnectionManager())
                .setDefaultRequestConfig(config)
                .setUserAgent("rgw-adm4j")
                .build();

    }

}