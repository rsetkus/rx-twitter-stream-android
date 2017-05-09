package co.uk.thejvm.thing.rxtwitter.stream;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import co.uk.thejvm.thingrxtwitter.R;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


public class StreamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("xBz7PXn20RfaBFBG46Gyjg")
                .setOAuthConsumerSecret("HdIEcbFcTH88BJmCHKWvjo8EONcAl119aOkyHyNupE4")
                .setOAuthAccessToken("410008204-U73IZZ707ScSaLB04ZiFO3R5cGJFvg3widi2owdW")
                .setOAuthAccessTokenSecret("WNIetyKFX9ufNb8qftijLsEX8PLRwcxfX51m8WC8CSCkc");


        StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {

                System.out.println(status.getUser().getName() + " : " + status.getText());
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(listener);
        twitterStream.filter("android");
    }
}
