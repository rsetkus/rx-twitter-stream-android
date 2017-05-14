package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.BasePresenter;
import co.uk.thejvm.thing.rxtwitter.common.util.PostExecutionScheduler;
import co.uk.thejvm.thing.rxtwitter.data.TweetViewModel;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TwitterAvatarRepository;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static io.reactivex.Flowable.just;
import static io.reactivex.Flowable.zip;

public class TwitterStreamPresenter implements BasePresenter<TwitterStreamView> {

    private TwitterStreamView twitterStreamView;
    private final TweetsRepository tweetsRepository;
    private final TwitterAvatarRepository avatarRepository;
    private final PostExecutionScheduler postExecutionScheduler;
    private CompositeDisposable disposable = new CompositeDisposable();

    public TwitterStreamPresenter(TweetsRepository tweetsRepository, TwitterAvatarRepository avatarRepository, PostExecutionScheduler thread) {
        this.tweetsRepository = tweetsRepository;
        this.avatarRepository = avatarRepository;
        this.postExecutionScheduler = thread;
    }

    public void connectToStream(List<String> terms) {

        tweetsRepository.getTweets(terms)
           // .subscribeOn(Schedulers.io()) < - should we write another scheduler for this to inject,  this currently breaks tests
            .flatMap(tweet ->
                zip(
                    just(tweet), avatarRepository.getAvatar(tweet.getImageUri()),
                    (tweet1, bitmap) -> new TweetViewModel(tweet1.getContent(), bitmap, tweet1.getDateLabel())
                )
            )
            .observeOn(postExecutionScheduler.getScheduler())
            .subscribe(new TweetStreamSubcriber());
    }

    @Override
    public void setView(TwitterStreamView view) {
        this.twitterStreamView = view;
    }

    @Override
    public void onPause() {
        disposable.clear();
    }

    public boolean isDisposed() {
        return disposable.size() == 0;
    }

    private class TweetStreamSubcriber extends DisposableSubscriber<TweetViewModel> {

        public TweetStreamSubcriber() {
            disposable.add(this);
        }

        @Override
        public void onNext(@NonNull TweetViewModel tweet) {
            twitterStreamView.renderTweet(tweet);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            // view.deadBird();
        }

        @Override
        public void onComplete() {
        }
    }
}
