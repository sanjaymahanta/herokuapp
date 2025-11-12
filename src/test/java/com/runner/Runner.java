package com.runner;


import com.baseclass.CustomizeTestNGCucumberRunner;

import io.cucumber.testng.CucumberOptions;



@CucumberOptions(
		
		features = "src/test/resources/Feature/Test.feature",
		glue = {"com.stepdef","com.hooks"},
		plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
		monochrome = true
		)
public class Runner extends CustomizeTestNGCucumberRunner {

}
