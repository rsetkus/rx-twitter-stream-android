package co.uk.thejvm.thing.rxtwitter.common.repository;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import twitter4j.TwitterStream;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StreamTweetsRepositoryTest {

    @Mock TwitterStream twitterStream;

    private StreamTweetsRepository streamTweetsRepository;

    @Before
    public void setUp() throws Exception {
        streamTweetsRepository = new StreamTweetsRepository(twitterStream);
    }

    @Test
    public void whenStartsListeningShouldFilterByTerms() {
        List<String> terms = Lists.newArrayList("android", "rxjava");
        streamTweetsRepository.getTweets(terms);
        verify(twitterStream).filter(terms.toArray(new String[terms.size()]));
    }

}