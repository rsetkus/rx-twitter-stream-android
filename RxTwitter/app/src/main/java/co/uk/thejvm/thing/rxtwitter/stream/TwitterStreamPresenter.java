package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.BasePresenter;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.observers.DisposableObserver;

public class TwitterStreamPresenter implements BasePresenter<TwitterStreamView> {

    private TwitterStreamView twitterStreamView;
    private final TweetsRepository tweetsRepository;
    protected Disposable disposable = Disposables.empty();

    public TwitterStreamPresenter(TweetsRepository tweetsRepository) {
        this.tweetsRepository = tweetsRepository;
    }

    public void connectToStream(List<String> terms) {
        disposable = tweetsRepository.getTweets(terms)
                .subscribeWith(new TweetStreamObserver());
    }

    @Override
    public void setView(TwitterStreamView view) {
        this.twitterStreamView = view;
    }

    @Override
    public void onPause() {
        disposable.dispose();
    }

    private class TweetStreamObserver extends DisposableObserver<Tweet> {

        @Override
        public void onNext(@NonNull Tweet tweet) {
            twitterStreamView.renderTweet(tweet);
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
