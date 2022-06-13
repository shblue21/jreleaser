package org.jreleaser.sdk.azure.api;

public enum ResourcesId {
    Memember("68ddce18-2501-45f1-a17b-7931a9922690"),
    Project("projects"),
    GIT("4e080c62-fa21-4fbc-8fef-2a10a2b38049"),
    COLLECTION("79bea8f8-c898-4965-8c51-8bbc3966faa8");
    public final String resourceId;

    private ResourcesId(String resourceId) {
        this.resourceId = resourceId;
    }
}
