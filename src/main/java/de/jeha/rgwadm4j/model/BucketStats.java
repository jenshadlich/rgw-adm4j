package de.jeha.rgwadm4j.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author jenshadlich@googlemail.com
 */
public class BucketStats {

    private final String bucket;
    private final String owner;
    private final long numObjects;
    private final long sizeKB;

    public BucketStats(String bucket, String owner, long numObjects, long sizeKB) {
        this.bucket = bucket;
        this.owner = owner;
        this.numObjects = numObjects;
        this.sizeKB = sizeKB;
    }

    public String getBucket() {
        return bucket;
    }

    public String getOwner() {
        return owner;
    }

    public long getNumObjects() {
        return numObjects;
    }

    public long getSizeKB() {
        return sizeKB;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
