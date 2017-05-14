package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.BasePresenter;
import co.uk.thejvm.thing.rxtwitter.common.util.PostExecutionThread;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TwitterStreamPresenter implements BasePresenter<TwitterStreamView> {

    private TwitterStreamView twitterStreamView;
    private final TweetsRepository tweetsRepository;
    private final PostExecutionThread postExecutionThread;
    private Disposable disposable = Disposables.empty();

    public TwitterStreamPresenter(TweetsRepository tweetsRepository, PostExecutionThread postExecutionThread) {
        this.tweetsRepository = tweetsRepository;
        this.postExecutionThread = postExecutionThread;
    }

    public void connectToStream(List<String> terms) {
        disposable = tweetsRepository.getTweets(terms)
                .observeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler())
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

    @Override
    public void dispose() {
        disposable.dispose();
    }

    public boolean isDisposed() {
        return disposable.isDisposed();
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
