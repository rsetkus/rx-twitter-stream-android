package co.uk.thejvm.thing.rxtwitter.common.di;

import android.app.Activity;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.stream.TwitterStreamPresenter;
import co.uk.thejvm.thing.rxtwitter.tweets.StreamTweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import dagger.Module;
import dagger.Provides;
import twitter4j.TwitterStream;

@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    public TweetsRepository provideTweetsRepository(TwitterStream twitterStream) {
        return new StreamTweetsRepository(twitterStream);
    }

    @ActivityScope
    @Provides
    public TwitterStreamPresenter provideTwitterStreamPresenter(TweetsRepository repository) {
        return new TwitterStreamPresenter(repository);
    }
}
