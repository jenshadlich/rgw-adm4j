package de.jeha.rgwadm4j;

import de.jeha.rgwadm4j.config.RgwAdmConfig;
import de.jeha.rgwadm4j.model.BucketStats;
import de.jeha.rgwadm4j.model.UserInfo;
import de.jeha.rgwadm4j.ops.GetBucketStats;
import de.jeha.rgwadm4j.ops.GetUserInfo;

import java.io.IOException;
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
        return new GetBucketStats(rgwAdmConfig).execute();
    }

    public UserInfo getUserInfo(String userId) throws IOException {
        return new GetUserInfo(rgwAdmConfig, userId).execute();
    }

}
