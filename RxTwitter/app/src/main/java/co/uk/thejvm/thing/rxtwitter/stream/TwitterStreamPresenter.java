package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import javax.inject.Inject;

import co.uk.thejvm.thing.rxtwitter.common.BasePresenter;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.observers.DisposableObserver;

public class TwitterStreamPresenter extends BasePresenter<TwitterStreamView> {

    private final TweetsRepository tweetsRepository;
    protected Disposable disposable = Disposables.empty();

    @Inject
    public TwitterStreamPresenter(TweetsRepository tweetsRepository) {
        this.tweetsRepository = tweetsRepository;
    }

    public void connectToStream(List<String> terms) {
        disposable = tweetsRepository.getTweets(terms)
                .subscribeWith(new TweetStreamObserver());
    }

    @Override
    public void onPause() {
        disposable.dispose();
    }

    private class TweetStreamObserver extends DisposableObserver<Tweet> {

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
