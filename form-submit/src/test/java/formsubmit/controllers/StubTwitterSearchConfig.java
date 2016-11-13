package formsubmit.controllers;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.social.twitter.api.Tweet;

import formsubmit.service.TwitterSearch;
@Configuration
public class StubTwitterSearchConfig {
	@Primary @Bean
    public TwitterSearch twitterSearch() {
        return (searchType, keywords) -> Arrays.asList(
                new Tweet(0, "tweetText", null, searchType, searchType, null, 0, searchType, searchType),
                new Tweet(0, "secondTweet", null, searchType, searchType, null, 0, searchType, searchType)
        );
    }
}
