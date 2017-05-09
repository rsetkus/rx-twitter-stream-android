package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.concurrent.atomic.AtomicBoolean;

import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class RxTwitterObservable extends Observable<Tweet> {

    private final TwitterStream twitterStream;

    public RxTwitterObservable(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    protected void subscribeActual(Observer<? super Tweet> observer) {
        RxStatusListener rxStatusListener = new RxStatusListener(twitterStream, observer);

        observer.onSubscribe(rxStatusListener);
        twitterStream.addListener(rxStatusListener);
    }

    static class RxStatusListener implements StatusListener, Disposable {

        private AtomicBoolean isDisposed = new AtomicBoolean(false);
        private final TwitterStream twitterStream;
        private final Observer<? super Tweet> observer;

        RxStatusListener(TwitterStream twitterStream, Observer<? super Tweet> observer) {
            this.twitterStream = twitterStream;
            this.observer = observer;
        }

        @Override
        public void onException(Exception ex) {
            observer.onError(ex);
        }

        @Override
        public void dispose() {
            isDisposed.set(true);
            twitterStream.removeListener(this);
        }

        @Override
        public boolean isDisposed() {
            return isDisposed.get();
        }

        @Override
        public void onStatus(Status status) {
            observer.onNext(new Tweet(status.getText()));
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {

        }

        @Override
        public void onStallWarning(StallWarning warning) {

        }
    }
}
