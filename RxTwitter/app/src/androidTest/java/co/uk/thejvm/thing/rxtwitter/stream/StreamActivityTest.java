package co.uk.thejvm.thing.rxtwitter.stream;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.common.collect.Lists;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.List;

import co.uk.thejvm.thing.rxtwitter.BaseActivityRule;
import co.uk.thejvm.thing.rxtwitter.RxTwitterApplication;
import co.uk.thejvm.thing.rxtwitter.TestModule;
import co.uk.thejvm.thing.rxtwitter.common.BasePresenter;
import co.uk.thejvm.thing.rxtwitter.common.di.ActivityModule;
import co.uk.thejvm.thing.rxtwitter.common.di.ApplicationModule;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class StreamActivityTest {

    private TwitterStreamPresenter mockTwitterStreamPresenter = mock(TwitterStreamPresenter.class);
    private ArgumentCaptor<TwitterStreamView> viewArgumentCaptor = ArgumentCaptor.forClass(TwitterStreamView.class);

    private static final boolean LAUNCH_ACTIVITY = false;
    private Context context = InstrumentationRegistry.getTargetContext();

    private List<String> fakeTerms = Lists.newArrayList("RxJava");
    private Tweet fakeTweet = new Tweet("go reactive or go home");

    @Rule
    public BaseActivityRule<StreamActivity> activityTestRule =
            new BaseActivityRule<StreamActivity>(StreamActivity.class, LAUNCH_ACTIVITY) {

                @Override
                public ApplicationModule getApplicationModule() {
                    RxTwitterApplication application = (RxTwitterApplication) context.getApplicationContext();
                    return new TestModule(application) {
                        @Override
                        protected ActivityModule getActivityModule() {
                            return new ActivityModule() {
                                @Override
                                public TwitterStreamPresenter provideTwitterStreamPresenter(TweetsRepository repository) {
                                    return mockTwitterStreamPresenter;
                                }
                            };
                        }
                    };
                }
            };

    /**
     * When twee received should render on recycler view.
     */
    @Test
    public void whenTweeReceived_ShouldRenderOnRecyclerView() {
        ResultRobot resultRobot = new StreamTweetActivityRobot().launchActivity(fakeTerms).verify();
        resultRobot.checkIfTweetTextIsVisibleOnScreen();
    }

    private class StreamTweetActivityRobot {
        private List<String> terms;

        public ResultRobot verify() {
            return new ResultRobot();
        }

        public StreamTweetActivityRobot launchActivity(List<String> terms) {
            this.terms = terms;

            captureView();
            stubStream();

            Intent startIntent = new Intent();
            activityTestRule.launchActivity(startIntent);
            return this;
        }

        private void captureView() {
            BasePresenter<TwitterStreamView> presenter = mockTwitterStreamPresenter;
            doNothing().when(presenter).setView(viewArgumentCaptor.capture());
        }

        private TwitterStreamView getTwitterStreamView() {
            return viewArgumentCaptor.getValue();
        }

        private void stubStream() {
            doAnswer(invocation -> new Handler().postAtFrontOfQueue(() -> getTwitterStreamView().renderTweet(fakeTweet)))
                    .when(mockTwitterStreamPresenter).connectToStream(terms);
        }
    }

    private class ResultRobot {

        public void checkIfTweetTextIsVisibleOnScreen() {
            onView(withText(fakeTweet.getContent())).check(matches(isDisplayed()));
        }
    }
}