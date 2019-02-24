# lideo-demo-app

This is demo application created as part of recruitment process in Lideo S.A.

Task was to create application to reserve flights on low cast airlines.
Application don't have to have UI - just backend.

Functional requirements:
1. Registering new clients
2. Client can search flights by: place of departure, place of arrival, departure time and minimal
available seats
3. Client reserve flight and have 2 days to pay:
* Registered client have 5% discount
* Client can bay faster check-in for 50 PLN
4. Client can cancel reservation 5 days before departure.

Non functional requirements:
1. Java 8
2. Payment gate is outside of application and API can be design by developer
3. Database not required
4. No UI - just backend
 
## Design decisions
I choose REST interface to expose functions of application. For programming stack I select:
1. Spring Boot as base - quick and easy to set up working app
2. Spring MVC - to expose REST endpoints
3. Spring Session - to manage sessions data (used in login clients)
4. Spring Data - to manage DB (I choose to use real SQL DB)
5. H2 - database just come with Spring Boot :-)
6. Cucumber - for behaviour and integration tests and prove that application do what should do.
7. Maven to build - I set up maven wrapper to make easy to build everywhere 
    
## Cucumber tests

### Registration of the clients

Test scenarios of registration clients are in [registration.feature](src/test/resources/registration.feature).
Step definitions are in [CommonStepdefs](src/test/java/integration/stepdefs/CommonStepdefs.java) and
[ReservationStepdefs](src/test/java/integration/stepdefs/reservation/ReservationStepdefs.java).
They are run by [RegistrationIntegrationTests](src/test/java/integration/RegistrationIntegrationTests.java).

Scenarios:
```gherkin
  Scenario: Register new client
  Scenario: Client can't register twice
  Scenario: Client can check if login is already used
  Scenario: Client can log in
```

