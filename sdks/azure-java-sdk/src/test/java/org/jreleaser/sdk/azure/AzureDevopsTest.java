package org.jreleaser.sdk.azure;

import org.jreleaser.util.SimpleJReleaserLoggerAdapter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class AzureDevopsTest {


    @Test
    public void testone() throws IOException, URISyntaxException {


        AzureDevops sdk = new AzureDevops(new SimpleJReleaserLoggerAdapter(SimpleJReleaserLoggerAdapter.Level.DEBUG),"bn5cc5xbvw7u5f3mes6jstwc2qkbl5y5n5m56szlb4wakerxffxa","shblue21",false,10000,10000);

        // AzureDevops azureDevops = AzureDevops
        //     .builder(new SimpleJReleaserLoggerAdapter(SimpleJReleaserLoggerAdapter.Level.DEBUG))
        //     .account("ACCOUNT")
        //     .apiKey("API_KEY")
        //     .build();
        sdk.findUser("shblue21@naver.com","jihun");
        sdk.deleteTag("shblue21@naver.com","jreleaserTest","jreleaser.git","annotagTest");

        System.out.println();

    }
}
