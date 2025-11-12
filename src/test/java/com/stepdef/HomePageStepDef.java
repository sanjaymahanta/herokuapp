package com.stepdef;

import com.pages.HomePage;
import com.utility.Log;

import io.cucumber.java.en.*;

public class HomePageStepDef {

	HomePage hp;

	@Given("the user is on the homepage")
	public void the_user_is_on_the_homepage() {
		hp = new HomePage();
	}

	@When("the user clicks on the {string} link")
	public void the_user_clicks_on_the_link(String string) {
		hp = new HomePage();
		hp.AddRemoveLink();
	}

	@Then("the {string} page should open")
	public void the_page_should_open(String string) {

	}

	@When("the user clicks on the {string} button")
	public void the_user_clicks_on_the_button(String string) {
		hp.addElementButton();
	}

	@Then("a new {string} button should appear on the page")
	public void a_new_button_should_appear_on_the_page(String string) {
		System.out.println("new button is there");
	}

	@Given("the user has already added a new element")
	public void the_user_has_already_added_a_new_element() {
		hp.AddRemoveLink();
		Log.info("User has added a new element before deletion");
	}

	@Then("the {string} button should be removed from the page")
	public void the_button_should_be_removed_from_the_page(String string) {
		hp.deleteElement();
	}
}
