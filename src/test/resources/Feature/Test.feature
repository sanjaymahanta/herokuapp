Feature: Add and Remove Elements functionality
  As a user
  I want to add and remove elements on the Add/Remove Elements page
  So that I can verify the buttons are added and removed dynamically

  Background:
    Given the user is on the homepage

  Scenario: Verify user can add a new element
    When the user clicks on the "Add/Remove Elements" link
    Then the "Add/Remove Elements" page should open
    When the user clicks on the "Add Element" button
    Then a new "Delete" button should appear on the page

  Scenario: Verify user can remove an element
    Given the user has already added a new element
    When the user clicks on the "Delete" button
    Then the "Delete" button should be removed from the page
