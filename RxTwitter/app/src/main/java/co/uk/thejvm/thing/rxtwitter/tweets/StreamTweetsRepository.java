package co.uk.thejvm.thing.rxtwitter.tweets;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.util.TwitterMapper;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import twitter4j.TwitterStream;

public class StreamTweetsRepository implements TweetsRepository {

    private final TwitterStream twitterStream;
    private final TwitterMapper twitterMapper;

    public StreamTweetsRepository(TwitterStream twitterStream, TwitterMapper twitterMapper) {
        this.twitterStream = twitterStream;
        this.twitterMapper = twitterMapper;
    }

    @Override
    public Observable<Tweet> getTweets(@NonNull List<String> terms) {
        return new RxTwitterObservable.Builder()
                .setTerms(terms)
                .setTwitterStream(twitterStream)
                .setTwitterMapper(twitterMapper)
                .build();
    }
}
