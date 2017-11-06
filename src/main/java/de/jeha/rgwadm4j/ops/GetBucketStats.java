package de.jeha.rgwadm4j.ops;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.jeha.rgwadm4j.config.RgwAdmConfig;
import de.jeha.rgwadm4j.model.BucketStats;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jenshadlich@googlemail.com
 */
public class GetBucketStats extends AbstractOperation<List<BucketStats>> {

    public GetBucketStats(RgwAdmConfig rgwAdmConfig) {
        super(rgwAdmConfig, "/admin/bucket?format=json&stats=true");
    }

    @Override
    protected HttpRequestBase buildRequest(String uri) {
        return new HttpGet(uri);
    }

    @Override
    protected List<BucketStats> mapResponse(InputStreamReader reader) {
        final List<BucketStats> bucketStats = new ArrayList<>();
        final JsonParser jsonParser = new JsonParser();

        for (JsonElement e : jsonParser.parse(reader).getAsJsonArray()) {
            final JsonObject o = e.getAsJsonObject();
            final JsonObject usageRgwMain = o.getAsJsonObject("usage").getAsJsonObject("rgw.main");

            final String bucket = o.get("bucket").getAsString();
            final String owner = o.get("owner").getAsString();

            final long numObject;
            final long sizeKB;
            if (usageRgwMain == null) {
                numObject = 0L;
                sizeKB = 0L;
            }else {
                numObject = usageRgwMain.get("num_objects").getAsLong();
                sizeKB = usageRgwMain.get("size_kb").getAsLong();
            }

            bucketStats.add(new BucketStats(bucket, owner, numObject, sizeKB));
        }

        return bucketStats;
    }

}
