package com.hooks;




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
	                // attach screenshot
	                scenario.attach(Util.takeScreenShot(), "image/png", scenario.getName());
	            } else {
	                System.out.println("No WebDriver instance found, skipping screenshot.");
	            }
	        } catch (Exception e) {
	            System.out.println("Error during cleanup: " + e.getMessage());
	        } finally {
	            // quit driver to free grid/node resources
	            DriverManager.quitDriver();
	        }
	    }
}