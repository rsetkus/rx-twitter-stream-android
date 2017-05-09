package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.BasePresenter;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class TwitterStreamPresenter extends BasePresenter<TwitterStreamView> {

    private final TweetsRepository tweetsRepository;

    public TwitterStreamPresenter(TweetsRepository tweetsRepository) {
        this.tweetsRepository = tweetsRepository;
    }

    public void connectToStream(List<String> terms) {
        tweetsRepository.getTweets(terms).subscribe(new TweetStreamObserver());
    }

    class TweetStreamObserver extends DisposableObserver<Tweet> {

        @Override
        public void onNext(@NonNull Tweet tweet) {
            view.renderTweet(tweet);
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
