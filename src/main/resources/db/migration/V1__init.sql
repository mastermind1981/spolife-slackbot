create table twitter_search (
  hashtag varchar(255) not null,
  last_tweet_id varchar(255) not null,
  primary key (hashtag)
);

insert into twitter_search(hashtag, last_tweet_id) values ('#spolife', '769572484970606592');
