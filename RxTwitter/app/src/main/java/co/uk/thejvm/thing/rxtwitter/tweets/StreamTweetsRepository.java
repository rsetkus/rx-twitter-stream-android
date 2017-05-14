package co.uk.thejvm.thing.rxtwitter.tweets;

import android.graphics.Bitmap;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.util.TwitterMapper;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import twitter4j.Status;
import twitter4j.TwitterStream;

import static io.reactivex.Observable.just;

public class StreamTweetsRepository implements TweetsRepository {

    private final TwitterStream twitterStream;
    private final TwitterMapper twitterMapper;

    public StreamTweetsRepository(TwitterStream twitterStream, TwitterMapper twitterMapper) {
        this.twitterStream = twitterStream;
        this.twitterMapper = twitterMapper;
    }

    @Override
    public Observable<Tweet> getTweets(@NonNull List<String> terms) {
        Observable<Status> statusObservable = new RxTwitterObservable.Builder()
                .setTerms(terms)
                .setTwitterStream(twitterStream)
                .build();

        return statusObservable
                .flatMap(s -> new RxUniversalImageLoader(s.getUser().getOriginalProfileImageURL())
                        .zipWith(just(s), (Bitmap b, Status status) -> twitterMapper.from(status, b)));
    }
}
