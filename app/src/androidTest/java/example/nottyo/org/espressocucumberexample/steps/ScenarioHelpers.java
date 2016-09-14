package example.nottyo.org.espressocucumberexample.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import example.nottyo.org.espressocucumberexample.utils.SpoonScreenshot;

/**
 * Created by traitanit on 9/14/2016 AD.
 */

public class ScenarioHelpers {
    private static Scenario scenario;

    @Before
    public static void before(final Scenario scenario){
        ScenarioHelpers.scenario = scenario;
    }

    public static Scenario getScenario(){
        return ScenarioHelpers.scenario;
    }

    @After
    public static void after(){
        if(ScenarioHelpers.scenario.isFailed()){
            SpoonScreenshot.takeScreenshot("FAILED");
        }
    }

}
