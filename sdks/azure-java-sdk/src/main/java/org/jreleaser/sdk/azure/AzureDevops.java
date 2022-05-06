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
package org.jreleaser.sdk.azure;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jreleaser.bundle.RB;
import org.jreleaser.model.JReleaserVersion;
import org.jreleaser.model.uploader.spi.UploadException;
import org.jreleaser.util.JReleaserLogger;

/**
 * @author JIHUN KIM
 * @since 1.1.0
 */

public class AzureDevops {
    public static final String RESOURCE_MAVEN = "6F7F8C07-FF36-473C-BCF3-BD6CC9B6C066"; // "https://pkgs.dev.azure.com/shblue21/"
    public static final String RESOURCE_PACKAGING = "7ab4e64e-c4d8-4f50-ae73-5ef2e21642a5"; // "https://feeds.dev.azure.com/shblue21/"
    private final JReleaserLogger logger;

    public AzureDevops(JReleaserLogger logger) {
        this.logger = logger;
    }

    public String getLocationUrl(String host, String organization, String resourceAreaID) throws UploadException {
        String resourceAreasUrl = new StringBuilder().append(host)
                .append(organization)
                .append("/_apis/resourceAreas/")
                .append(resourceAreaID)
                .toString();

        try {
            URL url = new URL(resourceAreasUrl.toString());
            logger.debug("resourceAreasUrl: {}", resourceAreasUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setAllowUserInteraction(false);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("User-Agent", "JReleaser/" + JReleaserVersion.getPlainVersion());
            connection.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            LocationUrl locationUrl = mapper.readValue(connection.getInputStream(), LocationUrl.class);

            return locationUrl.getLocationUrl();

        } catch (IOException e) {
            throw new UploadException(RB.$("ERROR_unexpected_upload"), e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class LocationUrl {
        private String id;

        private String name;

        private String locationUrl;

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

        public String getLocationUrl() {
            return locationUrl;
        }

        public void setLocationUrl(String locationUrl) {
            this.locationUrl = locationUrl;
        }
    }
}
