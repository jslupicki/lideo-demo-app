Feature: Registration of clients
  Clients can register itself in the system and then
  use login and password to identifying itself.

  Scenario: Register new client
    Given client with name A surname B and login L and password P
    And empty DB
    When send client by POST on /client/
    Then client appears in DB

  Scenario: Client can't register twice
    Given client with name A surname B and login L and password P
    And empty DB
    When send client by POST on /client/
    And send client by POST on /client/
    Then got response code 409 and body contains "already exist"

  Scenario: Client can check if login is already used
    Given empty DB
    When check login "L"
    Then result is false
    Given client with name A surname B and login L and password P
    And send client by POST on /client/
    When check login "L"
    Then result is true


