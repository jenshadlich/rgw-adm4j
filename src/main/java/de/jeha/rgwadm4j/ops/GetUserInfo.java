package de.jeha.rgwadm4j.ops;

import com.google.gson.JsonParser;
import de.jeha.rgwadm4j.config.RgwAdmConfig;
import de.jeha.rgwadm4j.model.UserInfo;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.InputStreamReader;

/**
 * @author jenshadlich@googlemail.com
 */
public class GetUserInfo extends AbstractOperation<UserInfo> {

    public GetUserInfo(RgwAdmConfig rgwAdmConfig, String userId) {
        super(rgwAdmConfig, "/admin/user?format=json&uid=" + userId);
    }

    @Override
    protected HttpRequestBase buildRequest(String uri) {
        return new HttpGet(uri);
    }

    @Override
    protected UserInfo mapResponse(InputStreamReader reader) {
        final JsonParser jsonParser = new JsonParser();

        final String userId = jsonParser.parse(reader).getAsJsonObject().get("user_id").getAsString();

        return new UserInfo(userId, null);
    }

}
