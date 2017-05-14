package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.BasePresenter;
import co.uk.thejvm.thing.rxtwitter.common.util.PostExecutionThread;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class TwitterStreamPresenter implements BasePresenter<TwitterStreamView> {

    private TwitterStreamView twitterStreamView;
    private final TweetsRepository tweetsRepository;
    private final PostExecutionThread postExecutionThread;
    private CompositeDisposable disposable = new CompositeDisposable();

    public TwitterStreamPresenter(TweetsRepository tweetsRepository, PostExecutionThread postExecutionThread) {
        this.tweetsRepository = tweetsRepository;
        this.postExecutionThread = postExecutionThread;
    }

    public void connectToStream(List<String> terms) {
        /* Observable...

        tweetsRepository.getTweets(terms)
            .observeOn(Schedulers.io())
            .observeOn(postExecutionThread.getScheduler())
            .subscribeWith(new TweetStreamObserver());*/

        tweetsRepository.getFlowableTweets(terms)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new TweetStreamSubcriber());
    }

    @Override
    public void setView(TwitterStreamView view) {
        this.twitterStreamView = view;
    }

    @Override
    public void onPause() {
        disposable.clear();
    }

    @Override
    public void dispose() {
        disposable.clear();
    }

    public boolean isDisposed() {
        return disposable.size() == 0;
    }

    private class TweetStreamObserver extends DisposableObserver<Tweet> {

        public TweetStreamObserver() {
            disposable.add(this);
        }

        @Override
        public void onNext(@NonNull Tweet tweet) {
            twitterStreamView.renderTweet(tweet);
        }

        @Override
        public void onError(@NonNull Throwable e) {}

        @Override
        public void onComplete() {}
    }

    private class TweetStreamSubcriber extends DisposableSubscriber<Tweet> {

        public TweetStreamSubcriber() {
            disposable.add(this);
        }

        @Override
        public void onNext(@NonNull Tweet tweet) {
            twitterStreamView.renderTweet(tweet);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            // view.deadBird();
        }

        @Override
        public void onComplete() {}
    }
}
