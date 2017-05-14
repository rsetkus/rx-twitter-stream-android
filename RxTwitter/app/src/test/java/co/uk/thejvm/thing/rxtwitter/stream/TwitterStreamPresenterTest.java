package co.uk.thejvm.thing.rxtwitter.stream;

import android.graphics.Bitmap;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.common.util.PostExecutionScheduler;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.data.TweetViewModel;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;
import co.uk.thejvm.thing.rxtwitter.tweets.TwitterAvatarRepository;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Flowable.just;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterStreamPresenterTest {

    @Mock private TwitterStreamView mockTwitterStreamView;
    @Mock private TweetsRepository mockTweetsRepository;
    @Mock private TwitterAvatarRepository mockTwitterAvatarRepository;
    @Mock private Bitmap mockBitmap;
    @Mock private Scheduler mockScheduler;
    private String imageUri = "http://pic.jpg";

    private TwitterStreamPresenter twitterStreamPresenter;

    @Before
    public void setUp() {
        twitterStreamPresenter = new TwitterStreamPresenter(mockTweetsRepository, mockTwitterAvatarRepository, new TestScheduler());
        twitterStreamPresenter.setView(mockTwitterStreamView);
    }

    @Test
    public void whenConnectedToStreamShouldRenderTweet() {
        List<String> terms = Lists.newArrayList("rxjava");
        when(mockTweetsRepository.getTweets(terms)).thenReturn(just(new Tweet("#android-rxjava", "2017.05.11 21:55", imageUri)));
        when(mockTwitterAvatarRepository.getAvatar(imageUri)).thenReturn(just(mockBitmap));
        twitterStreamPresenter.connectToStream(terms);

        verify(mockTweetsRepository).getTweets(terms);
        verify(mockTwitterAvatarRepository).getAvatar(imageUri);
        verify(mockTwitterStreamView).renderTweet(any(TweetViewModel.class));
    }

    @Test
    public void whenPausedShouldBeDisposed() {
        twitterStreamPresenter.onPause();
        assertTrue(twitterStreamPresenter.isDisposed());
    }

    private class TestScheduler implements PostExecutionScheduler {
        @Override
        public Scheduler getScheduler() {
            return Schedulers.newThread();
        }
    }
}