package co.uk.thejvm.thing.rxtwitter.tweets;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.stream.RxTwitterObservable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import twitter4j.TwitterStream;

public class StreamTweetsRepository implements TweetsRepository {

    private final TwitterStream twitterStream;

    public StreamTweetsRepository(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public Observable<Tweet> getTweets(@NonNull List<String> terms) {
        twitterStream.filter(terms.toArray(new String[terms.size()]));
        return new RxTwitterObservable(twitterStream);
    }
}
