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
    Then found flights 1,2

  Scenario: Search by arrival

  Scenario: Search by date

  Scenario: Search by range date

  Scenario: Search by number of people

  Scenario: Search by combinations

