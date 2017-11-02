package de.jeha.rgwadm4j.model;

import java.util.List;

/**
 * @author jenshadlich@googlemail.com
 */
public class UserInfo extends BaseModel {

    private final String userId;
    private final List<Key> keys;

    public UserInfo(String userId, List<Key> keys) {
        this.userId = userId;
        this.keys = keys;
    }

    public String getUserId() {
        return userId;
    }

    private static class Key {
        private final String user;
        private final String accessKey;
        private final String secretKey;

        public Key(String user, String accessKey, String secretKey) {
            this.user = user;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        public String getUser() {
            return user;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }
    }

}
