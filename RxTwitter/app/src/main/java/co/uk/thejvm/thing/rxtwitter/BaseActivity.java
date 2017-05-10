package co.uk.thejvm.thing.rxtwitter;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import co.uk.thejvm.thing.rxtwitter.common.di.ActivityComponent;
import co.uk.thejvm.thing.rxtwitter.common.di.ActivityModule;
import co.uk.thejvm.thing.rxtwitter.common.di.ApplicationModule;
import co.uk.thejvm.thing.rxtwitter.common.di.DaggerActivityComponent;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject();
    }

    private void inject() {
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationModule(new ApplicationModule((Application) getApplicationContext()))
                .activityModule(new ActivityModule())
                .build();

        activityComponent.inject(this);
    }
}
