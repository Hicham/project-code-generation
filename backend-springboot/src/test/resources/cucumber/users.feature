Feature: UserStep operations

  Scenario: Admin fetching a list of unapproved users
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/users/unapproved" is available for method "GET"
    When I access the endpoint "/api/users/unapproved" with method "GET"
    Then I should receive status code 200
    And I should receive a list of unapproved users

  Scenario: Regular user trying to access unapproved users list
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/users/unapproved" is available for method "GET"
    When I access the endpoint "/api/users/unapproved" with method "GET"
    Then I should receive status code 403

  Scenario: Finding a user by email when the user exists
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/users/{email}" is available for method "POST"
    When I access the endpoint "/api/users/{email}" with method "POST"
    Then I should receive status code 403


  Scenario: Finding a user by email when the user does not exist
    Given there is no user with email "nonexistent@example.com"
    And The endpoint for "/api/users/search" is available for method "POST"
    When the email "nonexistent@example.com" is searched for using endpoint "/api/users/search" with method "POST"
    Then a ResourceNotFoundException should be thrown

  Scenario: Creating a new user
    Given a new user with email "newuser@example.com" and password "Test123"
    And The endpoint for "/api/users" is available for method "POST"
    When the user "newuser@example.com" is created using endpoint "/api/users" with method "POST"
    Then the user with email "newuser@example.com" should be successfully created

  Scenario: Logging in with valid credentials
    Given a user with email "test@example.com" and password "hashedPassword"
    And The endpoint for "/api/login" is available for method "POST"
    When logging in with email "test@example.com" and password "password" using endpoint "/api/login" with method "POST"
    Then a token should be generated

  Scenario: Logging in with invalid password
    Given a user with email "test@example.com" and password "hashedPassword"
    And The endpoint for "/api/login" is available for method "POST"
    When logging in with email "test@example.com" and invalid password "invalidPassword" using endpoint "/api/login" with method "POST"
    Then an IllegalArgumentException should be thrown

  Scenario: Retrieving a list of not approved users
    Given there are users who are not approved
    And The endpoint for "/api/users/unapproved" is available for method "GET"
    When retrieving the list of not approved users using endpoint "/api/not-approved-users" with method "GET"
    Then a list of not approved users should be returned

  Scenario: Approving a user
    Given a user with ID 1 exists
    And The endpoint for "/api/users/1/approve" is available for method "PUT"
    When approving the user with ID 1 using endpoint "/api/users/1/approve" with method "PUT"
    Then the user with ID 1 should be approved

  Scenario: Getting a user by ID
    Given a user with ID 1 exists
    And The endpoint for "/api/users/1" is available for method "GET"
    When getting the user with ID 1 using endpoint "/api/users/1" with method "GET"
    Then the user with ID 1 should be returned

  Scenario: Getting a user by email when the user exists
    Given there is a user with email "test@example.com"
    And The endpoint for "/api/users/search" is available for method "POST"
    When getting the user with email "test@example.com" using endpoint "/api/users/search" with method "POST"
    Then the user with email "test@example.com" should be returned

  Scenario: Getting a list of users without providing an email
    Given there are users in the system
    And The endpoint for "/api/users" is available for method "GET"
    When retrieving a list of users without providing an email using endpoint "/api/users" with method "GET"
    Then a list of users should be returned

  Scenario: Getting a list of users by providing an email
    Given there are users in the system with email containing "test@example.com"
    And The endpoint for "/api/users/search" is available for method "POST"
    When retrieving a list of users by providing the email "test@example.com" using endpoint "/api/users/search" with method "POST"
    Then a list of users containing "test@example.com" should be returned
    
  Scenario: Admin approving a user
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    When I access the endpoint "/api/users/3/approve" with method "POST" and body:
    """
    {
      "userId": 3
    }
    """
    Then I should receive status code 200

  Scenario: Regular user trying to approve a user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    And The endpoint for "/api/users/3/approve" is available for method "POST"
    When I access the endpoint "/api/users/3/approve" with method "POST"
    Then I should receive status code 403
