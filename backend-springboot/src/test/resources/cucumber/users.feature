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
    And The endpoint for "/api/users/email" is available for method "GET"
    When I access the endpoint "/api/users/email" with method "GET"
    Then I should receive status code 403

  Scenario: User with specified email does not exist
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And The endpoint for "/api/users/email" is available for method "GET"
    When I access the endpoint "/api/users/userrr@gmail.com" with method "GET"
    Then I should receive status code 404

  Scenario: Creating a new user
    When I access the endpoint "/api/register" with method "POST" and body:
    """
    {
      "email": "testduha@gmail.com",
      "password": "Test123",
      "firstName": "Duha",
      "lastName": "Kahya",
      "BSNNumber": "123456789",
      "phoneNumber": "1234567890"
    }
    """
    Then I should receive status code 200

  Scenario: Logging in with valid credentials
    When I access the endpoint "/login" with method "POST" and body:
    """
    {
      "email" : "user@gmail.com",
      "password" : "Test123"
    }
    """
    Then I should receive status code 200

  Scenario: Logging in with invalid credentials
    When I access the endpoint "/login" with method "POST" and body:
    """
    {
      "email" : "usertest@gmail.com",
      "password" : "Test123"
    }
    """
    Then I should receive status code 500

  Scenario: Admin approving a user
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    When I access the endpoint "/api/users/3/approve" with method "POST" and body:
    """
    {
      "userId": 3,
       "transactionLimit": {
       "dailyLimit": 0
        },
      "absoluteLimit": 69
    }
    """
    Then I should receive status code 200

  Scenario: User approving a user
    Given user is logged in as "user" with username "user@gmail.com" password "Test123"
    When I access the endpoint "/api/users/3/approve" with method "POST" and body:
    """
    {
      "userId": 3,
       "transactionLimit": {
       "dailyLimit": 0
        },
      "absoluteLimit": 69
    }
    """
    Then I should receive status code 403

  Scenario: Getting a list of users without providing an email
    Given user is logged in as "admin" with username "hicham@gmail.com" password "Test123"
    And I access the endpoint "/api/users" with method "GET"
    Then I should receive status code 200
