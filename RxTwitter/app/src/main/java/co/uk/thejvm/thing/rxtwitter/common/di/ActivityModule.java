package co.uk.thejvm.thing.rxtwitter.common.di;

import android.app.Activity;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.common.util.PostExecutionThread;
import co.uk.thejvm.thing.rxtwitter.common.util.SimpleTwitterMapper;
import co.uk.thejvm.thing.rxtwitter.common.util.TwitterMapper;
import co.uk.thejvm.thing.rxtwitter.common.util.UIThread;
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
    public TwitterMapper provideTwitterMapper() {
        return new SimpleTwitterMapper();
    }

    @ActivityScope
    @Provides
    public TweetsRepository provideTweetsRepository(TwitterStream twitterStream, TwitterMapper twitterMapper) {
        return new StreamTweetsRepository(twitterStream, twitterMapper);
    }

    @ActivityScope
    @Provides
    public PostExecutionThread getPostExecutionThread() {
        return new UIThread();
    }

    @ActivityScope
    @Provides
    public TwitterStreamPresenter provideTwitterStreamPresenter(TweetsRepository repository, PostExecutionThread postExecutionThread) {
        return new TwitterStreamPresenter(repository, postExecutionThread);
    }
}
