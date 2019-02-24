Feature: Searching available flights
  Client can search flights by:
  1. Place of departure
  2. Place of arrival
  3. Date (range)
  4. Number of people

  Background:
    Given empty DB
    And load flights to DB:
      | id | departure | arrival | free_seats | price_per_seat | departure_time   |
      | 1  | Wroclaw   | Warsaw  | 10         | 115            | 2019-02-23 12:30 |
      | 2  | Wroclaw   | Poznan  | 20         | 125            | 2019-02-24 13:40 |
      | 3  | Bydgoszcz | Warsaw  | 30         |                | 2019-02-25 14:50 |
      | 4  | Bydgoszcz | Poznan  | 40         | 145            | 2019-02-26 15:20 |


  Scenario: Search by departure
    When search by:
      | departure |
      | wroclaw   |
# Demonstrate ignoring case
    Then found flights 1,2

  Scenario: Search by partial name of departure
    When search by:
      | departure |
      | wroc      |
    Then found flights 1,2

  Scenario: Search by arrival
    When search by:
      | arrival |
      | warsaw  |
    Then found flights 1,3

  Scenario: Search by range date
    When search by:
      | from_date  | to_date    |
      | 2019-02-23 | 2019-02-26 |
# Number 4 is missing because 2019-02-26 == 2019-02-26 00:00:00
    Then found flights 1,2,3

  Scenario: Search by number of available seats
    When search by:
      | seats |
      | 25    |
# Only flight 3 and 4 have > 25 free seats
    Then found flights 3,4

  Scenario: Search by combinations of filters
    When search by:
      | departure | arrival |
      | wro       | war     |
    Then found flights 1
    When search by:
      | departure | seats |
      | wro       | 15    |
    Then found flights 2
    When search by:
      | departure | arrival | seats | to_date    |
      | wro       | war     | 5     | 2019-02-24 |
    Then found flights 1

