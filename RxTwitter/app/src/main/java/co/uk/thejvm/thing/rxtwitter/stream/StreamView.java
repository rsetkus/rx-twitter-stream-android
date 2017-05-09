package co.uk.thejvm.thing.rxtwitter.stream;

import co.uk.thejvm.thing.rxtwitter.common.BaseView;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;

public interface StreamView extends BaseView {
    void renderTweet(Tweet tweet);
}
