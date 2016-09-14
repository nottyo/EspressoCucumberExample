package example.nottyo.org.espressocucumberexample.steps;


import android.app.Activity;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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


public class LoginActivitySteps {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    private Activity mActivity;

    @Before
    public void setUp(){
        mActivity = mActivityTestRule.getActivity();
        mActivityTestRule.launchActivity(null);
    }

    @Given("User specify username \"(.*)\"")
    public void specifyUsername(String username){
        ViewInteraction appCompatAutoCompleteTextView = onView(
                ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.email));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText(username));
    }

    @And("User specify password \"(.*)\"")
    public void specifyPassword(String password){
        ViewInteraction appCompatEditText = onView(
                ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.password));
        appCompatEditText.perform(scrollTo(), replaceText(password));
    }

    @When("User press login button")
    public void pressLoginButton(){
        ViewInteraction appCompatButton = onView(
                allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.email_sign_in_button), withText("Login"),
                        withParent(allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.email_login_form),
                                withParent(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.login_form))))));
        SpoonScreenshot.takeScreenshot(mActivity, "Login");
        appCompatButton.perform(scrollTo(), click());
    }

    @Then("Welcome text \"(.*)\" is displayed")
    public void welcomeTextAssertion(String welcomeText){
        ViewInteraction textView = onView(
                allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.welcome_message), withText("Welcome test@test.com"),
                        childAtPosition(
                                allOf(ViewMatchers.withId(example.nottyo.org.espressocucumberexample.R.id.welcome_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText(welcomeText)));
        SpoonScreenshot.takeScreenshot(mActivity, "Welcome");
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
