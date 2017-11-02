package de.jeha.rgwadm4j.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SignatureException;
import java.util.Locale;

/**
 * @author jenshadlich@googlemail.com
 */
public class AuthorizationUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationUtils.class);

    public static String computeAuthorization(String accessKey, String secretKey,
                                              String httpVerb, String date, String resource) {
        final String stringToSign =
                String.format(Locale.US,
                        "%s\n%s\n%s\n%s\n%s",
                        httpVerb,
                        "",
                        "",
                        date,
                        resource);
        LOG.debug("String to sign = '{}'", stringToSign);

        final String hmac;
        try {
            hmac = SignatureUtils.calculateHmacSha1(stringToSign, secretKey);
            LOG.debug("Calculated HMAC-SHA1 = '{}'", hmac);
        } catch (SignatureException e) {
            LOG.warn("Could not calculate HMAC-SHA1", e);
            return "";
        }

        return "AWS " + accessKey + ":" + hmac;
    }

}
