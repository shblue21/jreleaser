package org.jreleaser.sdk.azure.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag {

    private String name;
    private String objectId;
//    private String creator;
    private String url;
}
