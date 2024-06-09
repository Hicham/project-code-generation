Feature: TransactionsSteps operations

#  Background:
#    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"

  Scenario: Getting all transactions as admin
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/transactions" is available for method "GET"
    When I access the endpoint "/api/transactions" with method "GET"
    Then I should receive status code 200

  Scenario: Getting all transactions as user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/transactions" is available for method "GET"
    When I access the endpoint "/api/transactions" with method "GET"
    Then I should receive status code 403

  Scenario: Getting all transactions without token
    Given The endpoint for "/api/transactions" is available for method "GET"
    When I access the endpoint "/api/transactions" with method "GET"
    Then I should receive status code 403

  Scenario: Getting transactions of IBAN as admin
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/accounts/NL83INHO4762623440/transactions" is available for method "GET"
    When I access the endpoint "/api/accounts/NL83INHO4762623440/transactions" with method "GET"
    Then I should receive transactions of iban "NL83INHO4762623440"