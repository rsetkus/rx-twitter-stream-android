package co.uk.thejvm.thing.rxtwitter.stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.reactivestreams.Subscriber;

import twitter4j.TwitterStream;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RxTwitterFlowableTest {

    @Mock
    private TwitterStream mockTwitterStream;

    private RxTwitterFlowable rxTwitterFlowable;

    @Before
    public void setUp() {
        rxTwitterFlowable = new RxTwitterFlowable(mockTwitterStream);
    }

    @Test
    public void whenSubscribedShouldAddListener() {
        rxTwitterFlowable.subscribeActual(mock(Subscriber.class));
    }
}