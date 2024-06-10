Feature: AccountStep operations

  Background:
    Given headers are reset

  Scenario: Getting all accounts as admin
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/transactions" is available for method "GET"
    When I access the endpoint "/api/transactions" with method "GET"
    Then I should receive status code 200

  Scenario: Getting all accounts without token
    Given The endpoint for "/api/accounts" is available for method "GET"
    When I access the endpoint "/api/accounts" with method "GET"
    Then I should receive status code 403

  Scenario: Getting all accounts as a non-admin user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/accounts" is available for method "GET"
    When I access the endpoint "/api/accounts" with method "GET"
    Then I should receive status code 403

  Scenario: Getting checking accounts for a user as admin
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/users/1/accounts" is available for method "GET"
    When I access the endpoint "/api/users/1/accounts" with method "GET"
    Then I should receive the checking accounts for user 1

  Scenario: Getting checking accounts for a user as admin that does not exist
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/users/99999/accounts" is available for method "GET"
    When I access the endpoint "/api/users/99999/accounts" with method "GET"
    Then I should receive status code 404

  Scenario: Getting checking accounts for a user as user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/users/2/accounts" is available for method "GET"
    When I access the endpoint "/api/users/2/accounts" with method "GET"
    Then I should receive the checking accounts for user 2

  Scenario: Getting checking accounts for a user as other user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/users/1/accounts" is available for method "GET"
    When I access the endpoint "/api/users/1/accounts" with method "GET"
    Then I should receive status code 403

  Scenario: Getting checking accounts without token
    Given The endpoint for "/api/users/1/accounts" is available for method "GET"
    When I access the endpoint "/api/users/1/accounts" with method "GET"
    Then I should receive status code 403

