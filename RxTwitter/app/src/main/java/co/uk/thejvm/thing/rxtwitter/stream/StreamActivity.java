package co.uk.thejvm.thing.rxtwitter.stream;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import co.uk.thejvm.thing.rxtwitter.RxTwitterApplication;
import co.uk.thejvm.thing.rxtwitter.common.di.ActivityComponent;
import co.uk.thejvm.thing.rxtwitter.common.di.ActivityModule;
import co.uk.thejvm.thing.rxtwitter.common.di.DaggerActivityComponent;
import co.uk.thejvm.thingrxtwitter.R;


public class StreamActivity extends AppCompatActivity {

    @Inject
    TwitterStreamPresenter twitterStreamPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inject();
    }

    private void inject() {
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((RxTwitterApplication)getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule())
                .build();

        activityComponent.inject(this);
    }
}
