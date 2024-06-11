Feature: UserStep operations
  Scenario: Admin fetching a list of unapproved users
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/unapproved-users" is available for method "GET"
    When I access the endpoint "/api/unapproved-users" with method "GET"
    Then I should receive status code 200
    And I should receive a list of unapproved users

  Scenario: Regular user trying to access unapproved users list
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/unapproved-users" is available for method "GET"
    When I access the endpoint "/api/unapproved-users" with method "GET"
    Then I should receive status code 403