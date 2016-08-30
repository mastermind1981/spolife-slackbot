package io.pivotal.spo.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Matt Stine
 */
@Component
@ConfigurationProperties(prefix = "slack")
public class SlackConfiguration {

    private String token;
    private String channel;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
