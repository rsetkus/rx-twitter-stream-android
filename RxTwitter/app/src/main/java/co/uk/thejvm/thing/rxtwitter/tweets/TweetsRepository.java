package co.uk.thejvm.thing.rxtwitter.tweets;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface TweetsRepository {
    Observable<Tweet> getTweets(List<String> terms);

    Flowable<Tweet> getFlowableTweets(List<String> terms);
}
