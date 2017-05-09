package co.uk.thejvm.thing.rxtwitter.common.di;

import co.uk.thejvm.thing.rxtwitter.tweets.StreamTweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import dagger.Module;
import dagger.Provides;
import twitter4j.TwitterStream;

@Module
public class ActivityModule {

    @Provides
    public TweetsRepository provideTweetsRepository(TwitterStream twitterStream) {
        return new StreamTweetsRepository(twitterStream);
    }
}
