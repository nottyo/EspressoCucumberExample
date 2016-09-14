package example.nottyo.org.espressocucumberexample.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.util.Log;
import android.view.View;

import com.squareup.spoon.Spoon;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cucumber.api.Scenario;
import example.nottyo.org.espressocucumberexample.steps.ScenarioHelpers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;


/**
 * Created by traitanit on 9/12/2016 AD.
 */

public class SpoonScreenshot implements ViewAction {
    private static File screenshot;
    private String mTag;
    private String mTestClass;
    private String mTestMethod;
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test-screenshots/";

    public SpoonScreenshot(String tag, String testClass, String testMethod){
        mTag = tag;
        mTestClass = testClass;
        mTestMethod = testMethod;
    }

    private void writeScreenshotToFile(String fileName){
        FileInputStream screenshotStream;
        if (screenshot != null){
            try {
                screenshotStream = new FileInputStream(screenshot);
                final byte fileContent[] = new byte[(int) screenshot.length()];
                final int readImageBytes = screenshotStream.read(fileContent);
                if (readImageBytes != -1 ){
                    File outputDir = new File(dir);
                    if (!outputDir.mkdirs()){
                        Log.i(outputDir.getAbsolutePath(), "Creating directory: ");
                        outputDir.mkdir();
                    }
                    File outputFile = new File(outputDir, fileName + ".png");
                    FileOutputStream out = new FileOutputStream(outputFile);
                    out.write(fileContent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Activity getActivity(final View view){
        Context context = view.getContext();
        while(!(context instanceof Activity)){
            if (context instanceof ContextWrapper){
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                throw new IllegalStateException("Cannot find activity from class "+ context.getClass());
            }
        }
        return (Activity) context;
    }

    @Override
    public Matcher<View> getConstraints() {
        return Matchers.any(View.class);
    }

    @Override
    public String getDescription() {
        return "Taking screenshot using spoon";
    }

    @Override
    public void perform(UiController uiController, View view) {
        screenshot = Spoon.screenshot(getActivity(view), mTag, mTestClass, mTestMethod);
    }

    private static void embededScreenshotToScenario(Scenario scenario){
        if(scenario != null && screenshot != null) {
            FileInputStream screenshotStream;
            try{
                screenshotStream = new FileInputStream(screenshot);
                byte fileContent[] = new byte[(int) screenshot.length()];
                int readImageBytes = screenshotStream.read(fileContent);
                if(readImageBytes != -1){
                    scenario.embed(fileContent, "image/png");
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public static void takeScreenshot(String tag){
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String testClass = trace[3].getClassName();
        String testMethod = trace[3].getMethodName();
        onView(isRoot()).perform(new SpoonScreenshot(tag, testClass, testMethod));
        embededScreenshotToScenario(ScenarioHelpers.getScenario());
    }
}
