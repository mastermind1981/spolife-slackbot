package io.pivotal.spo.slack;

import io.pivotal.spo.search.TwitterSearch;
import io.pivotal.spo.search.TwitterSearchRepository;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Matt Stine
 */
@Service
public class SlackBot extends Bot {

    private final Logger logger = LoggerFactory.getLogger(SlackBot.class);

    private SlackConfiguration slackConfiguration;
    private TwitterTemplate template;
    private TwitterSearchRepository repository;
    private WebSocketSession session;

    //TODO: make this configuration
    private List<String> hashtags = Arrays.asList("#spolife");

    @Autowired
    public SlackBot(SlackConfiguration slackConfiguration,
                    TwitterTemplate twitterTemplate,
                    TwitterSearchRepository twitterSearchRepository) {
        this.slackConfiguration = slackConfiguration;
        this.template = twitterTemplate;
        this.repository = twitterSearchRepository;
    }

    @Override
    public String getSlackToken() {
        return slackConfiguration.getToken();
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.session = null;
    }

    @Transactional
    @Scheduled(fixedRate = 5000L)
    public void addLatestTweets() {
        hashtags.forEach(hashtag -> {
            TwitterSearch twitterSearch = repository.findOne(hashtag);

            logger.info("Max tweet ID for this search: {}", twitterSearch.getLastTweetId());

            List<Tweet> tweets = searchTwitterWith(twitterSearch);
            if (!tweets.isEmpty()) {
                tweets.forEach(this::postTweetToSlackChannel);

                String newMaxTweetId = tweets.get(0).getIdStr();
                twitterSearch.setLastTweetId(newMaxTweetId);

                logger.info("Saving future max tweet ID: {}", newMaxTweetId);

                repository.save(twitterSearch);
            }
        });
    }

    private List<Tweet> searchTwitterWith(TwitterSearch twitterSearch) {
        SearchParameters searchParameters = new SearchParameters(twitterSearch.getHashtag())
                .sinceId(Long.parseLong(twitterSearch.getLastTweetId()));
        SearchResults searchResults = template.searchOperations().search(searchParameters);
        return searchResults.getTweets();
    }

    private void postTweetToSlackChannel(Tweet tweet) {
        String uri = buildTweetUri(tweet);
        Message message = buildSlackBotMessage(uri);
        try {
            session.sendMessage(new TextMessage(message.toJSONString()));
        } catch (IOException e) {
            logger.error("Unable to send message to Slack", e);
            logger.error("Message was: {}", uri);
        }
    }

    private Message buildSlackBotMessage(String uri) {
        Message message = new Message(uri);
        message.setType(EventType.MESSAGE.name().toLowerCase());
        message.setText(encode(message.getText()));
        message.setChannel(slackConfiguration.getChannel());
        return message;
    }

    private String buildTweetUri(Tweet tweet) {
        return new StringBuilder("https://twitter.com/")
                .append(tweet.getFromUser())
                .append("/status/")
                .append(tweet.getIdStr())
                .toString();
    }

    private String encode(String message) {
        return message == null ? null : message.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
