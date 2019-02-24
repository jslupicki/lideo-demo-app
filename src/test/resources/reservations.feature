Feature: Client can reserve flight
  Client can reserve flight and have 2 days to pay for it.
  Registered clients have 5% discount.
  Client can purchase faster check-in by 50zł.
  Client can cancelling the reservation 5 days before departure.

  Background:
    Given empty DB
    And set time to "2019-02-20"
    And load flights to DB:
      | id | departure | arrival | free_seats | price_per_seat | departure_time   |
      | 1  | Wroclaw   | Warsaw  | 10         | 115            | 2019-02-23 12:30 |
      | 2  | Wroclaw   | Poznan  | 20         | 125            | 2019-02-24 13:40 |
      | 3  | Bydgoszcz | Warsaw  | 30         |                | 2019-02-25 14:50 |
      | 4  | Bydgoszcz | Poznan  | 40         | 145            | 2019-02-26 15:20 |
    And load clients to DB:
      | id | name | surname       | address | login | password |
      | 1  | Jan  | Kowalski      |         | log1  | pass1    |
      | 2  | Jan  | NotRegistered |         |       |          |
    And load payments to DB:
      | id | amount | external_id | paid  | created_at       |
      | 1  | 109.25 | someId      | false | 2019-02-23 12:30 |
    And load reservations to DB:
      | id | client_id | flight_id | payment_id | seats | price  | cancellation |
      | 1  | 1         | 1         | 1          | 1     | 109.25 | false        |

  Scenario: Non registered client reserve flight and don't have discount
    When client create reservation:
      | client_id | flight_id | seats | faster_check_in |
      | 2         | 1         | 2     | false           |
# 2 seats * 115 zł = 230 zł
    Then client have to pay 230 zł

  Scenario: Registered client reserve flight and have discount
    When client create reservation:
      | client_id | flight_id | seats | faster_check_in |
      | 1         | 1         | 2     | false           |
    Then client have to pay 218.5 zł
# 2 seats * 115 zł = 230 zł but discount 5% give 230 zł * 0.95 = 218.5 zł

  Scenario: Client purchase faster check-in and have to pay 50zł more
    When client create reservation:
      | client_id | flight_id | seats | faster_check_in |
      | 2         | 1         | 2     | true            |
    Then client have to pay 280 zł
# 2 seats * 115 zł = 230 zł plus 50 zł for faster check-in = 280 zł

  Scenario: Client cancel successfully the reservation 5 days before departure
    Given client log in by login "log1" and password "pass1"
# flight 1 departure 2019-02-23 12:30 - 5 days earlier is 2019-02-18
    And set time to "2019-02-18"
    When client cancel reservation 1
    Then reservation 1 is canceled

  Scenario: Client can't cancel the reservation in less than 5 days before departure
    Given client log in by login "log1" and password "pass1"
# flight 1 departure 2019-02-23 12:30 - 5 days earlier is 2019-02-18
    And set time to "2019-02-20"
    When client cancel reservation 1
    Then status is CONFLICT
    And response body contains "It is too late to cancel this reservation"

  Scenario: Client pay before 2 days everything go normal
    Given client log in by login "log1" and password "pass1"
# payment for reservation 1 was created at 2019-02-21 so client have time to 2019-02-23 before automatic cancelation
    And set time to "2019-02-21"
    When sleep for 2s
# check if scheduler cancel reservation
    Then reservation 1 is NOT canceled
    When client pay payment 1
    And status is OK
# 2019-02-23 01:00 scheduler should cancel reservation if it isn't payed
    And set time to "2019-02-23 01:00"
    And sleep for 2s
    Then reservation 1 is NOT canceled

  Scenario: Client not pay before 2 days and reservation is canceled
    Given client log in by login "log1" and password "pass1"
# payment for reservation 1 was created at 2019-02-21 so client have time to 2019-02-23 before automatic cancelation
    And set time to "2019-02-21"
    When sleep for 2s
# check if scheduler cancel reservation
    Then reservation 1 is NOT canceled
# now client NOT pay payment
# 2019-02-23 01:00 scheduler should cancel reservation if it isn't payed
    And set time to "2019-02-23 01:00"
    And sleep for 2s
    Then reservation 1 is canceled
