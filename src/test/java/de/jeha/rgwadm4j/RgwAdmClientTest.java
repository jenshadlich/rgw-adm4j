package de.jeha.rgwadm4j;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.jeha.rgwadm4j.model.BucketStats;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author jenshadlich@googlemail.com
 */
public class RgwAdmClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(RgwAdmClientTest.class);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(18080);

    @Before
    public void init() throws IOException {
        stubFor(any(urlPathEqualTo("/admin/bucket"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("stats", equalTo("true"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("simple_bucket_stats.json")));
    }

    @Test
    public void test() throws IOException {
        List<BucketStats> stats =
                new RgwAdmClient("A", "S", "http://localhost:18080").getBucketStats();

        LOG.debug("{}", stats.get(0));
        assertEquals(1, stats.size());
        assertEquals("my-bucket", stats.get(0).getBucket());
        assertEquals("the-owner", stats.get(0).getOwner());
        assertEquals(1234, stats.get(0).getSizeKB());
        assertEquals(42, stats.get(0).getNumObjects());
    }

}
