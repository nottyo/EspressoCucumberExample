package example.nottyo.org.espressocucumberexample.utils;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.squareup.spoon.Spoon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * Created by traitanit on 9/12/2016 AD.
 */

public class SpoonScreenshot {
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test-screenshots/";

    public static void takeScreenshot(Activity activity, String tag){
        try{
            File screenshot = Spoon.screenshot(activity, tag);
            writeScreenshotToFile(screenshot, tag);
        } catch (Exception e){
            e.printStackTrace();
            Log.w("Could not take screenshot with tag %s", tag);
        }
    }

    private static void writeScreenshotToFile(File screenshot, String tag){
        FileInputStream screenshotStream;
        if (screenshot != null){
            try {
                screenshotStream = new FileInputStream(screenshot);
                final byte fileContent[] = new byte[(int) screenshot.length()];
                final int readImageBytes = screenshotStream.read(fileContent);
                if (readImageBytes != -1 ){
                    File outputDir = new File(dir);
                    if (!outputDir.mkdirs()){
                        Log.i("Creating directory: %s", outputDir.getAbsolutePath());
                        outputDir.mkdir();
                    }
                    File outputFile = new File(outputDir, tag + ".png");
                    FileOutputStream out = new FileOutputStream(outputFile);
                    out.write(fileContent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
