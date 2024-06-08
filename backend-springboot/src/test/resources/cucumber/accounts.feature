Feature: AccountStep operations

  Background:
    Given user is logged in as admin with username "hicham@gmail.com" password "Test123"

  Scenario: Getting all accounts
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then I should receive all accounts

