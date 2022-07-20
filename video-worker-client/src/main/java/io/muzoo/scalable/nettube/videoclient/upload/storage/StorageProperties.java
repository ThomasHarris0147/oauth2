package io.muzoo.scalable.nettube.videoclient.upload.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public String getCdn() {
        return "d2tsi3g28pn9oo.cloudfront.net";
    }

    public void setLocation(String location) {
        this.location = location;
    }

}