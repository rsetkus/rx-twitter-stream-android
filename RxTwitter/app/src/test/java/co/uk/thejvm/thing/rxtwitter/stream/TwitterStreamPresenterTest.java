package co.uk.thejvm.thing.rxtwitter.stream;

import android.graphics.Bitmap;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import co.uk.thejvm.thing.rxtwitter.common.util.ExecutionScheduler;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.data.TweetViewModel;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TwitterAvatarRepository;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

import static io.reactivex.Flowable.just;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterStreamPresenterTest {

    @Mock private TwitterStreamView mockTwitterStreamView;
    @Mock private TweetsRepository mockTweetsRepository;
    @Mock private TwitterAvatarRepository mockTwitterAvatarRepository;
    @Mock private Bitmap mockBitmap;
    private String imageUri = "http://pic.jpg";

    private TwitterStreamPresenter twitterStreamPresenter;

    private TestScheduler testScheduler = new TestScheduler();
    private TestExecutionScheduler testExecutionScheduler = new TestExecutionScheduler();

    @Before
    public void setUp() {
        twitterStreamPresenter = new TwitterStreamPresenter(mockTweetsRepository, mockTwitterAvatarRepository,
            testExecutionScheduler, testExecutionScheduler, testExecutionScheduler);
        twitterStreamPresenter.setView(mockTwitterStreamView);
    }

    @Test
    public void whenConnectedToStreamShouldRenderTweet() {
        List<String> terms = Lists.newArrayList("rxjava");
        when(mockTweetsRepository.getTweets(terms)).thenReturn(just(new Tweet("#android-rxjava", "2017.05.11 21:55", imageUri)));
        when(mockTwitterAvatarRepository.getAvatar(imageUri)).thenReturn(just(mockBitmap));
        twitterStreamPresenter.connectToStream(terms);

        testScheduler.triggerActions();
        verify(mockTweetsRepository).getTweets(terms);
        verify(mockTwitterAvatarRepository).getAvatar(imageUri);
        verify(mockTwitterStreamView).renderTweet(any(TweetViewModel.class));
    }

    @Test
    public void whenPausedShouldBeDisposed() {
        twitterStreamPresenter.onPause();
        assertTrue(twitterStreamPresenter.isDisposed());
    }

    private class TestExecutionScheduler implements ExecutionScheduler {
        @Override
        public Scheduler getScheduler() {
            return testScheduler;
        }
    }
}