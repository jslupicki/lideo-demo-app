Feature: Registration of clients
  Clients can register itself in the system and then
  use login and password to identifying itself.

  Background:
    Given empty DB

  Scenario: Register new client
    Given client with name A surname B and login L and password P
    When register new client
    Then client appears in DB

  Scenario: Client can't register twice
    Given client with name A surname B and login L and password P
    When register new client
    And register new client
    Then got response code 409 and body contains "already exist"

  Scenario: Client can check if login is already used
    When check login "L"
    Then result is false
    Given client with name A surname B and login L and password P
    And register new client
    When check login "L"
    Then result is true

  Scenario: Client can log in
    Given client with name A surname B and login L and password P
    And register new client
    When get current client
    Then status is CONFLICT
    Given client log in by login "L" and password "P"
    When get current client
    Then status is OK
    And client have name A


