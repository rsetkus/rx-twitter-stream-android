package co.uk.thejvm.thing.rxtwitter.stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.reactivestreams.Subscriber;

import io.reactivex.Observer;
import twitter4j.TwitterStream;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RxTwitterFlowableTest {

    @Mock
    private TwitterStream mockTwitterStream;

    private RxTwitterObservable rxTwitterObservable;

    @Before
    public void setUp() {
        rxTwitterObservable = new RxTwitterObservable(mockTwitterStream);
    }

    @Test
    public void whenSubscribedShouldAddListener() {
        rxTwitterObservable.subscribeActual(mock(Observer.class));
    }
}