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

  Scenario: Disabling an account as admin
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And An account with IBAN "" exists
    And The endpoint for "/api/accounts/2/disable" is available for method "POST"
    When I access the endpoint "/api/accounts/disable/2" with method "POST"
    Then I should receive status code 200

  Scenario: Disabling an account as user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And An account with IBAN "" exists
    And The endpoint for "/api/accounts/2/disable" is available for method "POST"
    When I access the endpoint "/api/accounts/disable/2" with method "POST"
    Then I should receive status code 403

  Scenario: Enabling an account as admin
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And An account with IBAN "" exists
    And The endpoint for "/api/accounts/2/enable" is available for method "POST"
    When I access the endpoint "/api/accounts/enable/2" with method "POST"
    Then I should receive status code 200

  Scenario: Enabling an account as user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And An account with IBAN "" exists
    And The endpoint for "/api/accounts/2/enable" is available for method "POST"
    When I access the endpoint "/api/accounts/enable/2" with method "POST"
    Then I should receive status code 403

  Scenario: Admin approving a user
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/accounts/approve" is available for method "POST"
    When I access the endpoint "/accounts/approve" with method "POST" and body "{ \"userId\": 1, \"accountDetails\": { \"IBAN\": \"NL00RABO0123456789\" } }"
    Then I should receive status code 200
    And the response body should contain "User approved"

  Scenario: Regular user trying to approve a user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/accounts/approve" is available for method "POST"
    When I access the endpoint "/accounts/approve" with method "POST" and body "{ \"userId\": 1, \"accountDetails\": { \"IBAN\": \"NL00RABO0123456789\" } }"
    Then I should receive status code 403
