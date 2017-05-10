package co.uk.thejvm.thing.rxtwitter.common.di;

import javax.inject.Singleton;

import co.uk.thejvm.thing.rxtwitter.stream.TwitterStreamPresenter;
import co.uk.thejvm.thing.rxtwitter.tweets.StreamTweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import dagger.Module;
import dagger.Provides;
import twitter4j.TwitterStream;

@Module
public class ActivityModule {

    @Singleton
    @Provides
    public TweetsRepository provideTweetsRepository(TwitterStream twitterStream) {
        return new StreamTweetsRepository(twitterStream);
    }

    @Singleton
    @Provides
    public TwitterStreamPresenter provideTwitterStreamPresenter(TweetsRepository repository) {
        return new TwitterStreamPresenter(repository);
    }
}
