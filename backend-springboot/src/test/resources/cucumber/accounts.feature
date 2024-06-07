Feature: AccountController operations

  Scenario: Getting all accounts
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then I should receive all accounts

  Scenario: Getting checking accounts for a user
    Given The endpoint for "users/{userId}/accounts/checking" is available for method "GET"
    When I retrieve checking accounts for user with ID 1
    Then I should receive checking accounts for user with ID 1

  Scenario: Getting checking accounts for a user
    Given The endpoint for "users/{userId}/accounts/" is available for method "GET"
    When I retrieve accounts for user with ID 1
    Then I should receive accounts for user with ID 1

  Scenario: Disabling an account
    Given The endpoint for "accounts/disable/{IBAN}" is available for method "POST"
    When I disable account with IBAN "IBAN123"
    Then I should receive a confirmation that account is disabled

  Scenario: Enabling an account
    Given The endpoint for "accounts/enable/{IBAN}" is available for method "POST"
    When I enable account with IBAN "IBAN123"
    Then I should receive a confirmation that account is enabled

  Scenario: Setting transaction limits
    Given The endpoint for "accounts/setLimits/{IBAN}" is available for method "POST"
    When I set transaction limits for account with IBAN "IBAN123"
    Then I should receive a confirmation that transaction limits are updated
