package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import twitter4j.TwitterStream;

public class RxTwitterStream {

    private final TwitterStream twitterStream;

    public RxTwitterStream(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    public RxTwitterObservable bind(List<String> terms) {
        twitterStream.filter(terms.toArray(new String[terms.size()]));
        return new RxTwitterObservable(twitterStream);
    }
}
