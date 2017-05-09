package co.uk.thejvm.thing.rxtwitter.common.repository;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import io.reactivex.Flowable;
import twitter4j.TwitterStream;

public class StreamTweetsRepository implements TweetsRepository {

    private final TwitterStream twitterStream;

    public StreamTweetsRepository(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public Flowable<Tweet> getTweets(List<String> terms) {
        return null;
    }
}
