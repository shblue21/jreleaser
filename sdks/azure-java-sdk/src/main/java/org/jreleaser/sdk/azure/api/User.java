package org.jreleaser.sdk.azure.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String subjectKind;
    private String directoryAlias;
    private String domain;
    private String principalName;
    private String mailAddress;
    private String origin;
    private String originId;
    private String displayName;
    private Link _link;
    private String url;
    private String descriptor;

    public String getSubjectKind() {
        return subjectKind;
    }

    public void setSubjectKind(String subjectKind) {
        this.subjectKind = subjectKind;
    }

    public String getDirectoryAlias() {
        return directoryAlias;
    }

    public void setDirectoryAlias(String directoryAlias) {
        this.directoryAlias = directoryAlias;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Link get_link() {
        return _link;
    }

    public void set_link(Link _link) {
        this._link = _link;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }


}
