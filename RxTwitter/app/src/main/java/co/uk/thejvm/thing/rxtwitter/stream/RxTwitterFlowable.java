package co.uk.thejvm.thing.rxtwitter.stream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import io.reactivex.Flowable;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class RxTwitterFlowable extends Flowable<Tweet> {

    private final TwitterStream twitterStream;

    public RxTwitterFlowable(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    protected void subscribeActual(Subscriber<? super Tweet> subscriber) {
        RxStatusListener rxStatusListener = new RxStatusListener(twitterStream, subscriber);

        subscriber.onSubscribe(rxStatusListener);
        twitterStream.addListener(rxStatusListener);
    }

    static class RxStatusListener implements StatusListener, Subscription {

        private final TwitterStream twitterStream;
        private final Subscriber<? super Tweet> subscriber;

        RxStatusListener(TwitterStream twitterStream, Subscriber<? super Tweet> subscriber) {
            this.twitterStream = twitterStream;
            this.subscriber = subscriber;
        }

        @Override
        public void onStatus(Status status) {
            subscriber.onNext(new Tweet(status.getText()));
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

        @Override
        public void onException(Exception ex) {
            subscriber.onError(ex);
        }

        @Override
        public void request(long n) {

        }

        @Override
        public void cancel() {
            twitterStream.removeListener(this);
        }
    }
}
