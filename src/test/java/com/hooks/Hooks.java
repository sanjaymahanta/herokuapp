package com.hooks;




import org.openqa.selenium.WebDriver;

import com.baseclass.DriverManager;
import com.utility.Util;

import io.cucumber.java.After;

import io.cucumber.java.Before;

import io.cucumber.java.Scenario;

public class Hooks extends DriverManager {

	 @Before
	    public void beforeScenario(Scenario scenario) {
	        System.out.println(">>> Starting Scenario: " + scenario.getName());
	        // create driver based on config (local/remote)
	        DriverManager.createDriverFromConfig();
	    }

	
	    @After
	    public void afterScenario(Scenario scenario) {
	        System.out.println(">>> Ending Scenario: " + scenario.getName());
	        try {
	            if (DriverManager.webDriver.get() != null) {
	                WebDriver driver = DriverManager.webDriver.get();

	                // ✅ Attach screenshot to Cucumber report
	                scenario.attach(Util.takeScreenShot(), "image/png", scenario.getName());

	                // ✅ If remote run on LambdaTest, update status
	                String executionEnv = Util.properties("config", "execution_env").trim().toLowerCase();
	                String remoteProvider = "";
	                try {
	                    remoteProvider = Util.properties("config", "remote_provider").trim().toLowerCase();
	                } catch (Exception ignored) {}

	                if ("remote".equals(executionEnv) && "lambdatest".equals(remoteProvider)) {
	                    if (scenario.isFailed()) {
	                        ((org.openqa.selenium.JavascriptExecutor) driver)
	                                .executeScript("lambda-status=failed");
	                        System.out.println("⚠ Marked test FAILED on LambdaTest");
	                    } else {
	                        ((org.openqa.selenium.JavascriptExecutor) driver)
	                                .executeScript("lambda-status=passed");
	                        System.out.println("✅ Marked test PASSED on LambdaTest");
	                    }
	                }
	            } else {
	                System.out.println("No WebDriver instance found, skipping screenshot.");
	            }
	        } catch (Exception e) {
	            System.out.println("Error during cleanup: " + e.getMessage());
	        } finally {
	            // Quit driver to free LambdaTest node
	            DriverManager.quitDriver();
	        }
	    }

}