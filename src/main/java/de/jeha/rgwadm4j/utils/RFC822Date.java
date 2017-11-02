package de.jeha.rgwadm4j.utils;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.tz.FixedDateTimeZone;

import java.util.Date;
import java.util.Locale;

/**
 * @author jenshadlich@googlemail.com
 */
public class RFC822Date {

    private static final DateTimeZone DTZ_GMT =
            new FixedDateTimeZone("GMT", "GMT", 0, 0);

    private static final String DTF_PATTERN_GMT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern(DTF_PATTERN_GMT)
            .withLocale(Locale.US)
            .withZone(DTZ_GMT);

    /**
     * @param date the date
     * @return string representation of the given date
     */
    public static String format(Date date) {
        return DATE_FORMAT.print(date.getTime());
    }

}
