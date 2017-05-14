package co.uk.thejvm.thing.rxtwitter.common.di;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.common.util.PostExecutionScheduler;
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
    @Provides
    public PostExecutionScheduler getPostExecutionThread() {
        return new UIScheduler();
    }

    @ActivityScope
    @Provides
    public TwitterStreamPresenter provideTwitterStreamPresenter(TweetsRepository repository,
                                                                TwitterAvatarRepository avatarRepository,
                                                                PostExecutionScheduler postExecutionScheduler) {

        return new TwitterStreamPresenter(repository, avatarRepository, postExecutionScheduler);
    }
}
