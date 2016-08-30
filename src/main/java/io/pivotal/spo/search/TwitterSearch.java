package io.pivotal.spo.search;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Matt Stine
 */
@Entity
public class TwitterSearch {

    @Id
    private String hashtag;
    private String lastTweetId;

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getLastTweetId() {
        return lastTweetId;
    }

    public void setLastTweetId(String lastTweetId) {
        this.lastTweetId = lastTweetId;
    }
}
