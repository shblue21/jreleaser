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

/**
 * @author JIHUN KIM
 * @since 1.1.0
 */

public class AzureDevops {
    public static final String RESOURCE_MAVEN = "6F7F8C07-FF36-473C-BCF3-BD6CC9B6C066"; // "https://pkgs.dev.azure.com/shblue21/"
    public static final String RESOURCE_PACKAGING = "7ab4e64e-c4d8-4f50-ae73-5ef2e21642a5"; // "https://feeds.dev.azure.com/shblue21/"

    public String getLocationUrl(String host, String organization, String resourceAreaID){

        String url = new StringBuilder().append(host)
        .append(organization)
        .append("/_apis/resourceAreas/")
        .append(resourceAreaID)
        .toString();

        ClientUtils.

    }
}
