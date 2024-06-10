Feature: TransactionsSteps operations

  Background:
    Given headers are reset

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
    And The endpoint for "/api/accounts/IBANFAKE3/transactions" is available for method "GET"
    When I access the endpoint "/api/accounts/IBANFAKE3/transactions" with method "GET"
    Then I should receive transactions of iban "IBANFAKE3"

  Scenario: Getting transactions of IBAN as admin with wrong IBAN
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/accounts/NOIBAN/transactions" is available for method "GET"
    When I access the endpoint "/api/accounts/NOIBAN/transactions" with method "GET"
    Then I should receive status code 404

  Scenario: Getting transactions of IBAN as user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/accounts/IBANFAKE3/transactions" is available for method "GET"
    When I access the endpoint "/api/accounts/IBANFAKE3/transactions" with method "GET"
    Then I should receive transactions of iban "IBANFAKE3"

  Scenario: Getting transactions of IBAN as user without token
    Given The endpoint for "/api/accounts/IBANFAKE1/transactions" is available for method "GET"
    When I access the endpoint "/api/accounts/IBANFAKE1/transactions" with method "GET"
    Then I should receive status code 403


  Scenario: Deposit money to account
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    When I access the endpoint "/api/accounts/IBANFAKE3/deposit" with method "POST" and body:
    """
    {
      "amount": 100.00
    }
    """
    Then I should receive status code 200

  Scenario: Deposit money to account with negative amount
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    When I access the endpoint "/api/accounts/IBANFAKE3/deposit" with method "POST" and body:
    """
    {
      "amount": -100.00
    }
    """
    Then I should receive status code 500

  Scenario: Deposit money to account with negative amount of other account
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    When I access the endpoint "/api/accounts/IBANFAKE1/deposit" with method "POST" and body:
    """
    {
      "amount": 100.00
    }
    """
    Then I should receive status code 403

  Scenario: Deposit money to account with negative amount without account
    When I access the endpoint "/api/accounts/IBANFAKE1/deposit" with method "POST" and body:
    """
    {
      "amount": 100.00
    }
    """
    Then I should receive status code 403

  Scenario: Withdraw money from account
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    When I access the endpoint "/api/accounts/IBANFAKE3/withdraw" with method "POST" and body:
  """
  {
    "amount": 100.00
  }
  """
    Then I should receive status code 200

  Scenario: Withdraw money from account with negative amount
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    When I access the endpoint "/api/accounts/IBANFAKE3/withdraw" with method "POST" and body:
  """
  {
    "amount": -100.00
  }
  """
    Then I should receive status code 500

  Scenario: Withdraw money from account of another user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    When I access the endpoint "/api/accounts/IBANFAKE1/withdraw" with method "POST" and body:
  """
  {
    "amount": 100.00
  }
  """
    Then I should receive status code 403

  Scenario: Withdraw money from account without login
    When I access the endpoint "/api/accounts/IBANFAKE1/withdraw" with method "POST" and body:
  """
  {
    "amount": 100.00
  }
  """
    Then I should receive status code 403
