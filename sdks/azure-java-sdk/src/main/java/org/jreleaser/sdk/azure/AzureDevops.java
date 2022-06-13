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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import feign.Feign;
import feign.form.FormData;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.jreleaser.bundle.RB;
import org.jreleaser.model.releaser.spi.Asset;
import org.jreleaser.sdk.azure.api.*;
import org.jreleaser.sdk.commons.ClientUtils;
import org.jreleaser.sdk.commons.RestAPIException;
// import org.jreleaser.sdk.gitlab.api.FileUpload;
// import org.jreleaser.sdk.gitlab.api.GitlabAPI;
// import org.jreleaser.sdk.gitlab.api.LinkRequest;
// import org.jreleaser.sdk.gitlab.api.Milestone;
// import org.jreleaser.sdk.gitlab.api.Project;
// import org.jreleaser.sdk.gitlab.api.Release;
// import org.jreleaser.sdk.gitlab.api.User;
import org.jreleaser.util.CollectionUtils;
import org.jreleaser.util.JReleaserLogger;
import org.jreleaser.util.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.jreleaser.util.StringUtils.isBlank;
import static org.jreleaser.util.StringUtils.requireNonBlank;

/**
 * @author JIHUN KIM
 * @since 
 */
class AzureDevops {
    static final String ORG_URL_NEW = "https://dev.azure.com/{organization}";
    static final String ORG_URL_OLD = "https://{organization}.visualstudio.com.";
    private String orgUrl;
    private String organization;

    private static final String API_V7 = "?api-version=7.1-preview.1";

    private final JReleaserLogger logger;
    // private final AzureDevopsAPI api=null;
    private String apiHost=null;
    private AzureDevopsAPI api;

    // private User user;
    // private Project project;

    public AzureDevops(JReleaserLogger logger,
           String token,
           String organization,
           boolean isLegacy,
           int connectTimeout,
           int readTimeout) throws IOException {
        requireNonNull(logger, "'logger' must not be null");
        requireNonBlank(token, "'token' must not be blank");
        this.organization=organization;
        
        if (isLegacy) {
            this.orgUrl = ORG_URL_OLD.replace("{organization}", organization);
        } else {
            this.orgUrl = ORG_URL_NEW.replace("{organization}", organization);
        }

        this.logger = logger;

        ObjectMapper objectMapper = new ObjectMapper()
//        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.INDENT_OUTPUT, true);

        this.api = ClientUtils.builder(logger, connectTimeout, readTimeout)
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder(objectMapper))
            .requestInterceptor(template -> template.header("Authorization","Basic " + Base64.getEncoder().encodeToString(("" + ":" + token).getBytes())))
            .target(AzureDevopsAPI.class, orgUrl);
    }

    private String getLocationUrl(String resourceID, String organization){
        String url = new StringBuilder().append(orgUrl)
                .append("/_apis/resourceAreas/")
                .append(resourceID)
                .toString();

        HttpURLConnection con = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            node = mapper.readTree(con.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String locationUrl;
        if(StringUtils.isBlank(organization)){
            locationUrl = node.get("locationUrl").asText()+"_apis/";
        } else{
            locationUrl = node.get("locationUrl").asText()+organization+"/_apis/";
        }
        return locationUrl;
    }
    private String getLocationUrl(String resourceID) {
        return getLocationUrl(resourceID, null);
    }

    // Project findProject(String projectName, String identifier) throws RestAPIException {
    //     return getProject(projectName, identifier);
    // }

    // Optional<Milestone> findMilestoneByName(String owner, String repo, String identifier, String milestoneName) throws IOException {
    //     logger.debug(RB.$("git.milestone.lookup"), milestoneName, owner, repo);

    //     Project project = getProject(repo, identifier);

    //     try {
    //         List<Milestone> milestones = api.findMilestoneByTitle(project.getId(), CollectionUtils.<String, Object>map()
    //             .e("title", milestoneName));

    //         if (milestones == null || milestones.isEmpty()) {
    //             return Optional.empty();
    //         }

    //         Milestone milestone = milestones.get(0);
    //         return "active".equals(milestone.getState()) ? Optional.of(milestone) : Optional.empty();
    //     } catch (RestAPIException e) {
    //         if (e.isNotFound() || e.isForbidden()) {
    //             // ok
    //             return Optional.empty();
    //         }
    //         throw e;
    //     }
    // }

    // void closeMilestone(String owner, String repo, String identifier, Milestone milestone) throws IOException {
    //     logger.debug(RB.$("git.milestone.close"), milestone.getTitle(), owner, repo);

    //     Project project = getProject(repo, identifier);

    //     api.updateMilestone(CollectionUtils.<String, Object>map()
    //             .e("state_event", "close"),
    //         project.getId(), milestone.getId());
    // }

    // Project createProject(String owner, String repo) throws IOException {
    //     logger.debug(RB.$("git.project.create"), owner, repo);

    //     return api.createProject(repo, "public");
    // }

    // User getCurrentUser() throws RestAPIException {
    //     if (null == user) {
    //         logger.debug(RB.$("git.fetch.current.user"));
    //         user = api.getCurrentUser();
    //     }

    //     return user;
    // }

    // Project getProject(String projectName, String identifier) throws RestAPIException {
    //     if (null == project) {
    //         if (StringUtils.isNotBlank(identifier)) {
    //             logger.debug(RB.$("git.fetch.gitlab.project_by_id"), identifier);
    //             project = api.getProject(identifier.trim());
    //         } else {
    //             User u = getCurrentUser();

    //             logger.debug(RB.$("git.fetch.gitlab.project.by.user"), projectName, u.getUsername(), u.getId());
    //             List<Project> projects = api.getProject(u.getId(), CollectionUtils.<String, Object>map()
    //                 .e("search", projectName));

    //             if (projects == null || projects.isEmpty()) {
    //                 throw new RestAPIException(404, RB.$("ERROR_project_not_exist", projectName));
    //             }

    //             project = projects.get(0);
    //         }

    //         logger.debug(RB.$("git.gitlab.project.found"), project.getNameWithNamespace(), project.getId());
    //     }

    //     return project;
    // }

    // Release findReleaseByTag(String owner, String repoName, String identifier, String tagName) throws RestAPIException {
    //     logger.debug(RB.$("git.fetch.release.by.tag"), owner, repoName, tagName);

    //     Project project = getProject(repoName, identifier);

    //     try {
    //         return api.getRelease(project.getId(), urlEncode(tagName));
    //     } catch (RestAPIException e) {
    //         if (e.isNotFound() || e.isForbidden()) {
    //             // ok
    //             return null;
    //         }
    //         throw e;
    //     }
    // }

     void deleteTag(String owner, String organization,String repoName, String tagName) throws RestAPIException, URISyntaxException {
         logger.debug(RB.$("git.delete.tag.from"), tagName, owner, repoName);
         String resourceUrl=getLocationUrl(ResourcesId.GIT.resourceId,organization) ;

         String test=api.findTagId(new URI(resourceUrl),repoName,tagName);
         System.out.println(test);

//         api.deleteTag(project.getId(), urlEncode(tagName));
     }

    // void deleteRelease(String owner, String repoName, String identifier, String tagName) throws RestAPIException {
    //     logger.debug(RB.$("git.delete.release.from"), tagName, owner, repoName);

    //     Project project = getProject(repoName, identifier);

    //     api.deleteRelease(project.getId(), urlEncode(tagName));
    // }

    // void createRelease(String owner, String repoName, String identifier, Release release) throws RestAPIException {
    //     logger.debug(RB.$("git.create.release"), owner, repoName, release.getTagName());

    //     Project project = getProject(repoName, identifier);

    //     api.createRelease(release, project.getId());
    // }

    // void updateRelease(String owner, String repoName, String identifier, Release release) throws RestAPIException {
    //     logger.debug(RB.$("git.update.release"), owner, repoName, release.getTagName());

    //     Project project = getProject(repoName, identifier);

    //     api.updateRelease(release, project.getId());
    // }

    // Collection<FileUpload> uploadAssets(String owner, String repoName, String identifier, List<Asset> assets) throws IOException, RestAPIException {
    //     logger.debug(RB.$("git.upload.assets"), owner, repoName);

    //     List<FileUpload> uploads = new ArrayList<>();

    //     Project project = getProject(repoName, identifier);

    //     for (Asset asset : assets) {
    //         if (0 == Files.size(asset.getPath()) || !Files.exists(asset.getPath())) {
    //             // do not upload empty or non existent files
    //             continue;
    //         }

    //         logger.info(" " + RB.$("git.upload.asset"), asset.getFilename());
    //         try {
    //             FileUpload upload = api.uploadFile(project.getId(), toFormData(asset.getPath()));
    //             upload.setName(asset.getFilename());
    //             uploads.add(upload);
    //         } catch (IOException | RestAPIException e) {
    //             logger.error(" " + RB.$("git.upload.asset.failure"), asset.getFilename());
    //             throw e;
    //         }
    //     }

    //     return uploads;
    // }

    // void linkReleaseAssets(String owner, String repoName, Release release, String identifier, Collection<FileUpload> uploads) throws IOException, RestAPIException {
    //     logger.debug(RB.$("git.upload.asset.links"), owner, repoName, release.getTagName());

    //     Project project = getProject(repoName, identifier);

    //     for (FileUpload upload : uploads) {
    //         logger.debug(" " + RB.$("git.upload.asset.link"), upload.getName());
    //         try {
    //             api.linkAsset(upload.toLinkRequest(apiHost), project.getId(), release.getTagName());
    //         } catch (RestAPIException e) {
    //             logger.error(" " + RB.$("git.upload.asset.link.failure"), upload.getName());
    //             throw e;
    //         }
    //     }
    // }

    // void linkAssets(String owner, String repoName, Release release, String identifier, Collection<LinkRequest> links) throws IOException, RestAPIException {
    //     logger.debug(RB.$("git.upload.asset.links"), owner, repoName, release.getTagName());

    //     Project project = getProject(repoName, identifier);

    //     for (LinkRequest link : links) {
    //         logger.info(" " + RB.$("git.upload.asset.link"), link.getName());
    //         try {
    //             api.linkAsset(link, project.getId(), release.getTagName());
    //         } catch (RestAPIException e) {
    //             logger.error(" " + RB.$("git.upload.asset.link.failure"), link.getName());
    //             throw e;
    //         }
    //     }
    // }

    Optional<org.jreleaser.model.releaser.spi.User> findUser(String email, String name) {
        logger.debug(RB.$("git.user.lookup"), name, email);
        String resourceUrl=getLocationUrl(ResourcesId.COLLECTION.resourceId) ;

        UserResponse userResponse = null;
        try {
            Map reqMap = new HashMap();
            reqMap.put("query", email);
            reqMap.put("subjectKind", new String[]{"User"});
            userResponse = api.searchUser(new URI(resourceUrl),reqMap);
        } catch (URISyntaxException e) {
            logger.error("Failed to create URI for user search", e);
        }

        if(userResponse.getCount()>0 && userResponse!=null) {
            User user = userResponse.getUsers().get(0);
            return Optional.of(new org.jreleaser.model.releaser.spi.User(user.getDisplayName(), email, user.getUrl()));
        }

        try {
            Map reqMap = new HashMap();
            reqMap.put("query", name);
            reqMap.put("subjectKind", new String[]{"User"});
            userResponse = api.searchUser(new URI(resourceUrl),reqMap);
        } catch (URISyntaxException e) {
            logger.error("Failed to create URI for user search", e);
        }

        if(userResponse.getCount()>0 && userResponse!=null) {
            User user = userResponse.getUsers().get(0);
            return Optional.of(new org.jreleaser.model.releaser.spi.User(user.getDisplayName(), email, user.getUrl()));
        }

        return Optional.empty();
    }

    // private FormData toFormData(Path asset) throws IOException {
    //     return FormData.builder()
    //         .fileName(asset.getFileName().toString())
    //         .contentType(MediaType.parse(tika.detect(asset)).toString())
    //         .data(Files.readAllBytes(asset))
    //         .build();
    // }
}
