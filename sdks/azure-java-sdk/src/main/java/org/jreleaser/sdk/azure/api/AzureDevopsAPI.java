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

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import feign.form.FormData;
import org.jreleaser.infra.nativeimage.annotations.ProxyConfig;

import java.util.List;
import java.util.Map;

/**
 * @author JIHUN KIM
 * @since 1.1.0
 */
@ProxyConfig
public interface AzureDevopsAPI {
    @RequestLine("GET /{organization}/_apis/resourceAreas/{resourceAreaId}")
    ResourceUrl getLocationUrl(@Param("organization") String organization, @Param("resourceAreaId") String resourceAreaId);    

    @RequestLine("PUT {locationUrl}/{project}/_packaging/{feed}/maven/v1/{packageId}/{version}/{fileName}")
    void publishMvnPackage(@Param("locationUrl") String locationUrl, @Param("project") String project,
            @Param("feed") String feed, @Param("packageId") String packageId, @Param("version") String version,
            @Param("fileName") String fileName);

    @RequestLine("GET {locationUrl}/{project}/_apis/packaging/feeds/{feed}/maven/groups/{groupId}/artifacts/{artifactId}/versions/{version}")
    void getMvnPackageVersion(@Param("locationUrl") String locationUrl, @Param("project") String project,
            @Param("feed") String feed, @Param("groupId") String groupId, @Param("artifactId") String artifactId,
            @Param("version") String version);
}
