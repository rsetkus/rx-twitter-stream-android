package co.uk.thejvm.thing.rxtwitter.common.di;

import javax.inject.Named;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.common.util.ExecutionScheduler;
import co.uk.thejvm.thing.rxtwitter.common.util.IOScheduler;
import co.uk.thejvm.thing.rxtwitter.common.util.SimpleTwitterMapper;
import co.uk.thejvm.thing.rxtwitter.common.util.TwitterMapper;
import co.uk.thejvm.thing.rxtwitter.common.util.UIScheduler;
import co.uk.thejvm.thing.rxtwitter.stream.TwitterStreamPresenter;
import co.uk.thejvm.thing.rxtwitter.tweets.ImageLoaderTwitterAvatarRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.StreamTweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TwitterAvatarRepository;
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
    public TwitterAvatarRepository provideTwitterAvatarRepository() {
        return new ImageLoaderTwitterAvatarRepository();
    }

    @ActivityScope
    @Provides @Named("ui")
    public ExecutionScheduler getUiExecutionScheduler() {
        return new UIScheduler();
    }

    @ActivityScope
    @Provides @Named("io")
    public ExecutionScheduler getIoExecutionScheduler() {
        return new IOScheduler();
    }

    @ActivityScope
    @Provides
    public TwitterStreamPresenter provideTwitterStreamPresenter(TweetsRepository repository,
                                                                TwitterAvatarRepository avatarRepository,
                                                                @Named("ui") ExecutionScheduler uiScheduler,
                                                                @Named("io") ExecutionScheduler ioScheduler) {

        return new TwitterStreamPresenter(repository, avatarRepository, uiScheduler, ioScheduler);
    }
}
