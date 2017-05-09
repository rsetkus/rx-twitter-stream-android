package co.uk.thejvm.thing.rxtwitter.stream;

import java.util.List;

import twitter4j.TwitterStream;

public class RxTwitterStream {

    private final TwitterStream twitterStream;

    public RxTwitterStream(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    public RxTwitterFlowable bind(List<String> terrms) {
        twitterStream.filter(terrms.toArray(new String[terrms.size()]));
        return new RxTwitterFlowable(twitterStream);
    }
}
