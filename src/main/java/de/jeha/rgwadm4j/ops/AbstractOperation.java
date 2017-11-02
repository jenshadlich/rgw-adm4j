package de.jeha.rgwadm4j.ops;

import de.jeha.rgwadm4j.config.RgwAdmConfig;
import de.jeha.rgwadm4j.utils.AuthorizationUtils;
import de.jeha.rgwadm4j.utils.RFC822Date;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author jenshadlich@googlemail.com
 */
public abstract class AbstractOperation<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractOperation.class);

    private final RgwAdmConfig rgwAdmConfig;
    private final String pathAndQuery;

    AbstractOperation(RgwAdmConfig rgwAdmConfig, String pathAndQuery) {
        this.rgwAdmConfig = rgwAdmConfig;
        this.pathAndQuery = pathAndQuery;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public T execute() throws IOException {
        final CloseableHttpClient httpClient = buildHttpClient();
        final HttpRequestBase request = buildRequest(rgwAdmConfig.getEndpoint() + pathAndQuery);
        final String dateString = RFC822Date.format(new Date());
        final String authorization = AuthorizationUtils.computeAuthorization(
                rgwAdmConfig.getAccessKey(),
                rgwAdmConfig.getSecretKey(),
                request.getMethod(),
                dateString,
                request.getURI().getPath()
        );

        request.addHeader("Date", dateString);
        request.addHeader("Authorization", authorization);

        final HttpResponse response = httpClient.execute(request);

        LOG.debug("{}", response.getStatusLine());

        return mapResponse(new InputStreamReader(response.getEntity().getContent()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    protected abstract HttpRequestBase buildRequest(String uri);

    protected abstract T mapResponse(InputStreamReader reader);

    // -----------------------------------------------------------------------------------------------------------------

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
