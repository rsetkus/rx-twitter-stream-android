package co.uk.thejvm.thing.rxtwitter.stream;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.R;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;

public class StreamActivity extends BaseActivity implements TwitterStreamView {

    private static final String TAG = "StreamActivity";

    private RecyclerView liveTweets;
    private TweetsAdapter tweetsAdapter = new TweetsAdapter();

    private static final List<String> TERMS = Lists.newArrayList("android");
    private static final int RECENT_TWEET_POSITION = 0;

    @Inject
    TwitterStreamPresenter twitterStreamPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        liveTweets = (RecyclerView) findViewById(R.id.live_tweets_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        liveTweets.setLayoutManager(mLayoutManager);
        liveTweets.setAdapter(tweetsAdapter);

        twitterStreamPresenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        twitterStreamPresenter.connectToStream(TERMS);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void renderTweet(Tweet tweet) {
        tweetsAdapter.insertNewTweet(tweet);
        liveTweets.smoothScrollToPosition(RECENT_TWEET_POSITION);
    }

    @Override
    protected void setUpDependencies() {
        getActivityComponent().inject(this);
    }

    private class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

        private List<Tweet> tweets = Lists.newArrayList();

        @Override
        public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
            return new TweetViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TweetViewHolder holder, int position) {
            holder.setTweet(tweets.get(position));
        }

        @Override
        public int getItemCount() {
            return tweets.size();
        }

        public void insertNewTweet(Tweet tweet) {
            tweets.add(RECENT_TWEET_POSITION, tweet);
            notifyItemInserted(RECENT_TWEET_POSITION);
        }
    }

    private class TweetViewHolder extends RecyclerView.ViewHolder {

        private TextView tweetContent;
        private TextView tweetCreatedDateLabel;

        public TweetViewHolder(View itemView) {
            super(itemView);

            tweetContent = (TextView) itemView.findViewById(R.id.tweet_content);
            tweetCreatedDateLabel = (TextView) itemView.findViewById(R.id.tweet_created_date_label);
        }

        public void setTweet(Tweet tweet) {
            tweetContent.setText(tweet.getContent());
            tweetCreatedDateLabel.setText(tweet.getDateLabel());
        }
    }
}
