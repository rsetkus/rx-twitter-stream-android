package co.uk.thejvm.thing.rxtwitter;

import android.app.Application;

import co.uk.thejvm.thing.rxtwitter.common.di.ActivityModule;
import co.uk.thejvm.thing.rxtwitter.common.di.ApplicationModule;

/**
 * Dagger2 test module used for espresso tests
 */
public class TestModule extends ApplicationModule {

    public TestModule(Application application) {
        super(application);
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule();
    }
}
