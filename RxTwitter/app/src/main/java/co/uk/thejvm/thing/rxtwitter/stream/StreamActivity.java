package co.uk.thejvm.thing.rxtwitter.stream;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.R;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;

public class StreamActivity extends BaseActivity implements TwitterStreamView {

    @Inject
    TwitterStreamPresenter twitterStreamPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twitterStreamPresenter.setView(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void renderTweet(Tweet tweet) {

    }
}
