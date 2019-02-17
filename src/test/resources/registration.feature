Feature: Registration of clients
  Clients can register itself in the system and then
  use login and password to identifying itself.

  Scenario: Register new client
    Given client with name A surname B and login L and password P
    And empty DB
    When send client by POST on /client/
    Then client appears in DB

  Scenario: Client can't register twice

  Scenario: Client can check if login is already used

