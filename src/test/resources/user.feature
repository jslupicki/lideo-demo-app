Feature: Get all user

  Scenario: Get all users
    When client call get /users
    Then receiver got list of users
    And list of users have user with id = 1
