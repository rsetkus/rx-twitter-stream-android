package co.uk.thejvm.thing.rxtwitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import co.uk.thejvm.thing.rxtwitter.common.di.ActivityComponent;
import co.uk.thejvm.thing.rxtwitter.common.di.ApplicationComponent;
import co.uk.thejvm.thing.rxtwitter.common.di.DaggerActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
    }

    private void inject() {
        ApplicationComponent applicationComponent = ((RxTwitterApplication) getApplicationContext()).getApplicationComponent();
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(applicationComponent.getModuleBootstrapper().getNewActivityModule(this))
                .build();

        activityComponent.inject(this);
        setUpDependencies();
    }

    protected ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    protected abstract void setUpDependencies();
}
