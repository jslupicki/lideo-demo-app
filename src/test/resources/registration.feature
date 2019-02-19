Feature: Registration of clients
  Clients can register itself in the system and then
  use login and password to identifying itself.

  Background:
    Given empty DB

  Scenario: Register new client
    Given client with name A surname B and login L and password P
    When send client by POST on /client/
    Then client appears in DB

  Scenario: Client can't register twice
    Given client with name A surname B and login L and password P
    When send client by POST on /client/
    And send client by POST on /client/
    Then got response code 409 and body contains "already exist"

  Scenario: Client can check if login is already used
    When check login "L"
    Then result is false
    Given client with name A surname B and login L and password P
    And send client by POST on /client/
    When check login "L"
    Then result is true

  Scenario: Client can log in
    Given client with name A surname B and login L and password P
    And send client by POST on /client/
    When get current client
    Then status is NO_CONTENT
    Given client log in by login "L" and password "P"
    When get current client
    Then status is OK
    And client have name A


