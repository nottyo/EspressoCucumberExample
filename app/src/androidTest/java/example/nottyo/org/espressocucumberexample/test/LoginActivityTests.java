package example.nottyo.org.espressocucumberexample.test;


import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import example.nottyo.org.espressocucumberexample.*;
import example.nottyo.org.espressocucumberexample.utils.SpoonScreenshot;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTests {

    private Activity mActivity;
    private PowerManager.WakeLock mFullWakeUpLock;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp(){
        mActivity = mActivityTestRule.getActivity();
        turnOnScreen();
        unlockScreen();
    }

    private void turnOnScreen(){
        PowerManager powerManager = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        mFullWakeUpLock = powerManager.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "FULL WAKE UP LOCK");
        mFullWakeUpLock.acquire();
    }

    private void unlockScreen(){
        KeyguardManager keyguard = (KeyguardManager) mActivity.getSystemService(Context.KEYGUARD_SERVICE);
        keyguard.newKeyguardLock(getClass().getSimpleName()).disableKeyguard();
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

    }

    private void turnOffScreen(){
        if (mFullWakeUpLock != null){
            mFullWakeUpLock.release();
        }
    }

    @Test
    public void testLogin() {
        ViewInteraction appCompatAutoCompleteTextView = onView(
                ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.email));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("test@test.com"));

        ViewInteraction appCompatEditText = onView(
                ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.password));
        appCompatEditText.perform(scrollTo(), replaceText("2234567"));

        ViewInteraction appCompatButton = onView(
                allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.email_sign_in_button), withText("Login"),
                        withParent(allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.email_login_form),
                                withParent(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.login_form))))));
        SpoonScreenshot.takeScreenshot(mActivityTestRule.getActivity(), "Login");
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.welcome_message), withText("Welcome test@test.com"),
                        childAtPosition(
                                allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.welcome_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Welcome test@test.com")));
        SpoonScreenshot.takeScreenshot(mActivityTestRule.getActivity(), "Welcome");

    }

    @After
    public void tearDown(){
        turnOffScreen();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
