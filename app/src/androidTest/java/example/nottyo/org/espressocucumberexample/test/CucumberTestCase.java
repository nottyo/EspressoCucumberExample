package example.nottyo.org.espressocucumberexample.test;

import cucumber.api.CucumberOptions;

/**
 * Created by traitanit on 9/14/2016 AD.
 */


@CucumberOptions(features = "features",
                glue = {"example.nottyo.org.espressocucumberexample.steps"},
                format = {"pretty",
                          "json:/storage/sdcard0/test-screenshots/cucumber-reports/cucumber.json",
                          "html:/storage/sdcard0/test-screenshots/cucumber-reports/cucumber-html-report"})
public class CucumberTestCase {
}
