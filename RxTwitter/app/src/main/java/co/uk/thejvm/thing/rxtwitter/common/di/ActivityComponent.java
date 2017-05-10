package co.uk.thejvm.thing.rxtwitter.common.di;

import javax.inject.Singleton;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.stream.StreamActivity;
import dagger.Component;

@Singleton
@Component(dependencies = ApplicationComponent.class, modules = {ApplicationModule.class, ActivityModule.class})
public interface ActivityComponent {
    void inject(BaseActivity baseActivity);
    void inject(StreamActivity streamActivity);
}
