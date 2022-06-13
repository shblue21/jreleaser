package org.jreleaser.sdk.azure.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "_links")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {
    private String self;
    private String edit;
    private String avatar;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

     class Self {
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }

     class Memberships {

        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }

     class MembershipState {

        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }

     class StorageKey {

        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }


    }

     class Avatar {
         private String href;

         public String getHref() {
             return href;
         }

         public void setHref(String href) {
             this.href = href;
         }
     }
}
