package co.uk.thejvm.thing.rxtwitter;

import android.app.Application;

import co.uk.thejvm.thing.rxtwitter.common.di.ActivityModule;
import co.uk.thejvm.thing.rxtwitter.common.di.ApplicationModule;
import co.uk.thejvm.thing.rxtwitter.common.di.ModuleBootstrapper;

/**
 * Dagger2 test module used for espresso tests
 */
public class TestModule extends ApplicationModule {

    public TestModule(Application application) {
        super(application);
    }

    @Override
    protected ModuleBootstrapper provideModuleBootstrapper() {
        return new ModuleBootstrapper() {
            @Override
            public ActivityModule getNewActivityModule(BaseActivity baseActivity) {
                return getActivityModule(baseActivity);
            }
        };
    }

    protected ActivityModule getActivityModule(BaseActivity baseActivity) {
        return new ActivityModule(baseActivity);
    }
}
