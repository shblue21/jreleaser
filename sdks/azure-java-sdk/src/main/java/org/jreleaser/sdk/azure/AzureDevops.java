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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.jreleaser.bundle.RB;
import org.jreleaser.model.Artifact;
import org.jreleaser.model.JReleaserVersion;
import org.jreleaser.model.Project;
import org.jreleaser.model.uploader.spi.UploadException;
import org.jreleaser.sdk.azure.api.AzureDevopsAPI;
import org.jreleaser.sdk.azure.api.ResourceUrl;
import org.jreleaser.sdk.commons.ClientUtils;
import org.jreleaser.sdk.commons.RestAPIException;
import org.jreleaser.util.JReleaserLogger;

import feign.Feign;
import feign.Request;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author JIHUN KIM
 * @since 1.1.0
 */

public class AzureDevops {

    public static final String RESOURCE_MAVEN = "6F7F8C07-FF36-473C-BCF3-BD6CC9B6C066"; // "https://pkgs.dev.azure.com/shblue21/"
    public static final String RESOURCE_PACKAGING = "7ab4e64e-c4d8-4f50-ae73-5ef2e21642a5"; // "https://feeds.dev.azure.com/shblue21/"
    public static final String RESOURCE_WIKI = "BF7D82A0-8AA5-4613-94EF-6172A5EA01F3"; // "https://feeds.dev.azure.com/shblue21/"

    private final JReleaserLogger logger;
    private final AzureDevopsAPI api;

    public AzureDevops(JReleaserLogger logger, String apiHost, String token,
            int connectTimeout, int readTimeout) throws IOException, UploadException {
        requireNonNull(logger, "'logger' must not be null");
        this.logger = logger;

        ObjectMapper objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true);

        this.api = ClientUtils.builder(logger, connectTimeout, readTimeout)
            .encoder(new FormEncoder(new JacksonEncoder(objectMapper)))
            .decoder(new JacksonDecoder(objectMapper))
            .requestInterceptor(template -> template.header("Authorization", String.format("Basic %s", token)))
            .target(AzureDevopsAPI.class, apiHost);
    }

    public ResourceUrl getLocationUrl(String organization, String resourceAreaID) {
        return api.getLocationUrl(organization, resourceAreaID);
    }

    public void publishArtifacts(String organization, String azureProject, String feed, String path, List<Artifact> artifacts) throws UploadException {
        String locationUrl= getLocationUrl(organization, RESOURCE_MAVEN).getLocationUrl();
        for(Artifact artifact : artifacts) {
            
        }


        // api.publishMvnPackage(locationUrl, project, feed, packageId, version,
        // fileName);
    }


    // public void createRelease(String owner, String repoName, String identifier,
    // Release release) throws RestAPIException {
    // logger.debug(RB.$("git.create.release"), owner, repoName,
    // release.getTagName());

    // Project project = getProject(repoName, identifier);

    // api.createWiki(release, project.getId());
    // }
}
