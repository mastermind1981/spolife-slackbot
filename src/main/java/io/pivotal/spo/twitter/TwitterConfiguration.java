package io.pivotal.spo.twitter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Matt Stine
 */
@Component
@ConfigurationProperties(prefix = "spring.social.twitter")
public class TwitterConfiguration {

    private String appId;
    private String appSecret;
    private String accessToken;
    private String accessSecret;

    @Bean
    public TwitterTemplate twitterTemplate() {
        return new TwitterTemplate(appId,
                appSecret,
                accessToken,
                accessSecret);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
}
