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

/**
 * @author JIHUN KIM
 * @since 1.1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WikiPage {
    String content;
    String gitItemPath;
    Integer id;
    Boolean isNonConformant;
    Boolean isParentPage;
    Integer order;
    String path;
    String remoteUrl;
    WikiPage[] subPages;
    String url;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGitItemPath() {
        return gitItemPath;
    }

    public void setGitItemPath(String gitItemPath) {
        this.gitItemPath = gitItemPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsNonConformant() {
        return isNonConformant;
    }

    public void setIsNonConformant(Boolean isNonConformant) {
        this.isNonConformant = isNonConformant;
    }

    public Boolean getIsParentPage() {
        return isParentPage;
    }

    public void setIsParentPage(Boolean isParentPage) {
        this.isParentPage = isParentPage;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public WikiPage[] getSubPages() {
        return subPages;
    }

    public void setSubPages(WikiPage[] subPages) {
        this.subPages = subPages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
