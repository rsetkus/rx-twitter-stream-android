package co.uk.thejvm.thing.rxtwitter.stream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.R;
import co.uk.thejvm.thing.rxtwitter.common.BackPressureStrategy;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;

public class StreamActivity extends BaseActivity implements TwitterStreamView {

    private static final String TAG = "StreamActivity";

    @Inject
    TwitterStreamPresenter twitterStreamPresenter;

    static final String BACKPRESSURE_STRATEGY_EXTRA_KEY = "backpressure_strategy_option";
    private Toolbar toolbar;
    private boolean isSearchOpened = false;
    private MenuItem mSearchAction;

    private EditText termsSearch;
    private RecyclerView liveTweets;

    private TweetsAdapter tweetsAdapter = new TweetsAdapter();
    private BackPressureStrategy backPressureStrategy = BackPressureStrategy.NO_STRATEGY;

    private static final int RECENT_TWEET_POSITION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        liveTweets.setLayoutManager(mLayoutManager);
        liveTweets.setAdapter(tweetsAdapter);

        twitterStreamPresenter.setView(this);

        Intent intent = getIntent();
        if (intent.hasExtra(BACKPRESSURE_STRATEGY_EXTRA_KEY)) {
            backPressureStrategy = (BackPressureStrategy) intent.getSerializableExtra(BACKPRESSURE_STRATEGY_EXTRA_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
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

    private void handleMenuSearch() {
        ActionBar supportActionBar = getSupportActionBar();

        if (isSearchOpened){
            disableSearch(supportActionBar);
        } else {

            supportActionBar.setDisplayShowCustomEnabled(true);
            supportActionBar.setCustomView(R.layout.search_bar);
            supportActionBar.setDisplayShowTitleEnabled(false);

            termsSearch = (EditText)supportActionBar.getCustomView().findViewById(R.id.terms_search);

            termsSearch.setOnEditorActionListener((view, id, event) -> {
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(termsSearch.getText().toString());
                    disableSearch(supportActionBar);
                    return true;
                }
                return false;
            });

            termsSearch.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            mSearchAction.setIcon(getResources().getDrawable(R.mipmap.ic_clear_white_24dp, null));

            isSearchOpened = true;
        }
    }

    private void disableSearch(ActionBar action) {
        action.setDisplayShowCustomEnabled(false);
        action.setDisplayShowTitleEnabled(true);

        termsSearch.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        mSearchAction.setIcon(getResources().getDrawable(R.mipmap.ic_search_white_24dp, null));

        isSearchOpened = false;
    }

    private void doSearch(String term) {
        tweetsAdapter.clear();
        twitterStreamPresenter.dispose();
        twitterStreamPresenter.connectToStream(Lists.newArrayList(term));
    }

    private void bindViews() {
        setContentView(R.layout.activity_stream);

        liveTweets = (RecyclerView) findViewById(R.id.live_tweets_list);
        termsSearch = (EditText) findViewById(R.id.terms_search);
        toolbar = (Toolbar) findViewById(R.id.rx_twitter_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static Intent createIntent(Context context, BackPressureStrategy strategy) {
        Intent intent = new Intent(context, StreamActivity.class);
        intent.putExtra(BACKPRESSURE_STRATEGY_EXTRA_KEY, strategy);
        return intent;
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

        public void clear() {
            tweets.clear();
            notifyDataSetChanged();
        }
    }

    private class TweetViewHolder extends RecyclerView.ViewHolder {

        private TextView tweetContent;
        private TextView tweetCreatedDateLabel;
        private ImageView avatar;

        public TweetViewHolder(View itemView) {
            super(itemView);

            tweetContent = (TextView) itemView.findViewById(R.id.tweet_content);
            tweetCreatedDateLabel = (TextView) itemView.findViewById(R.id.tweet_created_date_label);
            avatar = (ImageView) itemView.findViewById(R.id.profile_avatar);
        }

        public void setTweet(Tweet tweet) {
            tweetContent.setText(tweet.getContent());
            tweetCreatedDateLabel.setText(tweet.getDateLabel());

            /*Optional<Bitmap> bitmapOptional = Optional.fromNullable(tweet.getAvatarImage());
            if (bitmapOptional.isPresent()) {
                avatar.setImageBitmap(bitmapOptional.get());
            }*/
        }
    }
}
