package co.uk.thejvm.thing.rxtwitter.stream;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TwitterStreamPresenterTest {

    @Mock private TwitterStreamView mockTwitterStreamView;
    @Mock private TweetsRepository mockTweetsRepository;

    private TwitterStreamPresenter twitterStreamPresenter;

    @Before
    public void setUp() {
        twitterStreamPresenter = new TwitterStreamPresenter(mockTweetsRepository);
        twitterStreamPresenter.setView(mockTwitterStreamView);
    }

    @Test
    public void whenConnectedToStreamShouldRenderTweet() {
        List<String> terms = Lists.newArrayList("rxjava");
        twitterStreamPresenter.connectToStream(terms);

        verify(mockTweetsRepository).getTweets(terms);
        verify(mockTwitterStreamView).renderTweet(any(Tweet.class));
    }
}