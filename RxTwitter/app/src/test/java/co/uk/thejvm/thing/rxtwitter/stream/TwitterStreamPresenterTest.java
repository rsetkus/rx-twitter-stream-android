package co.uk.thejvm.thing.rxtwitter.stream;

import android.graphics.Bitmap;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.util.PostExecutionThread;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterStreamPresenterTest {

    @Mock private TwitterStreamView mockTwitterStreamView;
    @Mock private TweetsRepository mockTweetsRepository;
    @Mock private Bitmap mockBitmap;
    @Mock private Scheduler mockScheduler;

    private TwitterStreamPresenter twitterStreamPresenter;

    @Before
    public void setUp() {
        twitterStreamPresenter = new TwitterStreamPresenter(mockTweetsRepository, new TestPostExecutionThread());
        twitterStreamPresenter.setView(mockTwitterStreamView);
    }

    @Test
    public void whenConnectedToStreamShouldRenderTweet() {
        List<String> terms = Lists.newArrayList("rxjava");
        when(mockTweetsRepository.getTweets(terms)).thenReturn(Observable.just(new Tweet("#android-rxjava", mockBitmap, "2017.05.11 21:55")));
        twitterStreamPresenter.connectToStream(terms);

        verify(mockTweetsRepository).getTweets(terms);
        verify(mockTwitterStreamView).renderTweet(any(Tweet.class));
    }

    @Test
    public void whenPausedShouldBeDisposed() {
        twitterStreamPresenter.onPause();
        assertTrue(twitterStreamPresenter.isDisposed());
    }

    private class TestPostExecutionThread implements PostExecutionThread {

        @Override
        public Scheduler getScheduler() {
            return Schedulers.newThread();
        }
    }
}