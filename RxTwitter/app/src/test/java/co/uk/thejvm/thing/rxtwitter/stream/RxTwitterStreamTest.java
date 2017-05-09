package co.uk.thejvm.thing.rxtwitter.stream;

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
public class RxTwitterStreamTest {

    @Mock
    private TwitterStream twitterStream;

    private RxTwitterStream rxTwitterStream;
    private final static List<String> TERMS = Lists.newArrayList("android");

    @Before
    public void setUp() throws Exception {
        rxTwitterStream = new RxTwitterStream(twitterStream);
    }

    @Test
    public void whenBindedShouldShouldFilterByTerms() {
        rxTwitterStream.bind(TERMS);
        verify(twitterStream).filter(TERMS.toArray(new String[TERMS.size()]));
    }
}