package co.uk.thejvm.thing.rxtwitter.stream;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import co.uk.thejvm.thing.rxtwitter.BaseActivity;
import co.uk.thejvm.thing.rxtwitter.BaseActivityRule;
import co.uk.thejvm.thing.rxtwitter.RxTwitterApplication;
import co.uk.thejvm.thing.rxtwitter.TestModule;
import co.uk.thejvm.thing.rxtwitter.common.di.ActivityModule;
import co.uk.thejvm.thing.rxtwitter.common.di.ApplicationModule;
import co.uk.thejvm.thing.rxtwitter.data.Tweet;
import co.uk.thejvm.thing.rxtwitter.tweets.TweetsRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class StreamActivityTest {

    private TwitterStreamPresenter mockTwitterStreamPresenter = mock(TwitterStreamPresenter.class);
    private ArgumentCaptor<TwitterStreamView> viewArgumentCaptor = ArgumentCaptor.forClass(TwitterStreamView.class);

    private static final boolean LAUNCH_ACTIVITY = false;
    private Context context = InstrumentationRegistry.getTargetContext();

    private Tweet fakeTweet = new Tweet("go reactive or go home");

    @Rule
    public BaseActivityRule<StreamActivity> activityTestRule =
            new BaseActivityRule<StreamActivity>(StreamActivity.class, LAUNCH_ACTIVITY) {

                @Override
                public ApplicationModule getApplicationModule() {

                    RxTwitterApplication application = (RxTwitterApplication) context.getApplicationContext();

                    return new TestModule(application) {

                        @Override
                        protected ActivityModule getActivityModule(BaseActivity baseActivity) {

                            return new ActivityModule(baseActivity) {
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
        ResultRobot resultRobot = new StreamTweetActivityRobot().launchActivity().verify();
        resultRobot.checkIfTweetTextIsVisibleOnScreen();
    }

    private class StreamTweetActivityRobot {

        public ResultRobot verify() {
            return new ResultRobot();
        }

        public StreamTweetActivityRobot launchActivity() {

            captureView();
            stubStream();

            Intent startIntent = new Intent();
            activityTestRule.launchActivity(startIntent);
            return this;
        }

        private void captureView() {
            doNothing().when(mockTwitterStreamPresenter).setView(viewArgumentCaptor.capture());
        }

        private TwitterStreamView getTwitterStreamView() {
            return viewArgumentCaptor.getValue();
        }

        private void stubStream() {
            doAnswer(invocation -> new Handler().postAtFrontOfQueue(() -> getTwitterStreamView().renderTweet(fakeTweet)))
                    .when(mockTwitterStreamPresenter).connectToStream(any());
        }
    }

    private class ResultRobot {

        public void checkIfTweetTextIsVisibleOnScreen() {
            onView(withText(fakeTweet.getContent())).check(matches(isDisplayed()));
        }
    }
}