package co.uk.thejvm.thing.rxtwitter;

import android.app.Application;

import co.uk.thejvm.thing.rxtwitter.common.di.ApplicationComponent;
import co.uk.thejvm.thing.rxtwitter.common.di.DaggerApplicationComponent;

public class RxTwitterApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    protected void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
