/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2022 The JReleaser authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.sdk.azure.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author JIHUN KIM
 * @since 1.1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Package {
    private ReferenceLinks _link;
    private String deletedDate;
    private String id;
    private String name;
    private String permanentlyDeletedDate;
    private UpstreamSourceInfo[] sourceChain;
    private String version;

    public ReferenceLinks get_link() {
        return _link;
    }

    public void set_link(ReferenceLinks _link) {
        this._link = _link;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermanentlyDeletedDate() {
        return permanentlyDeletedDate;
    }

    public void setPermanentlyDeletedDate(String permanentlyDeletedDate) {
        this.permanentlyDeletedDate = permanentlyDeletedDate;
    }

    public UpstreamSourceInfo[] getSourceChain() {
        return sourceChain;
    }

    public void setSourceChain(UpstreamSourceInfo[] sourceChain) {
        this.sourceChain = sourceChain;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    static class ReferenceLinks {
        private Object links;

        public Object getLinks() {
            return links;
        }

        public void setLinks(Object links) {
            this.links = links;
        }
    }

    static class UpstreamSourceInfo {
        private String displayLocation;
        private String id;
        private String location;
        private String name;
        private PackagingSourceType sourceType;

        public String getDisplayLocation() {
            return displayLocation;
        }

        public void setDisplayLocation(String displayLocation) {
            this.displayLocation = displayLocation;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PackagingSourceType getSourceType() {
            return sourceType;
        }

        public void setSourceType(PackagingSourceType sourceType) {
            this.sourceType = sourceType;
        }
    }

    static class PackagingSourceType {
        private String internal;

        @JsonProperty("public")
        private String _public;

        public String getInternal() {
            return internal;
        }

        public void setInternal(String internal) {
            this.internal = internal;
        }

        public String get_public() {
            return _public;
        }

        public void set_public(String _public) {
            this._public = _public;
        }
    }
}
