package co.uk.thejvm.thing.rxtwitter.common.di;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;

public class ModuleBootstrapper {

    public ActivityModule getNewActivityModule(BaseActivity baseActivity) {
        return new ActivityModule(baseActivity);
    }
}
