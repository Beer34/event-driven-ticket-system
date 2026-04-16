Feature: Booking Service API

Scenario: Get all events
  Given url 'http://localhost:8082/events'
  When method GET
  Then status 200

Scenario: Get seats for event
  Given url 'http://localhost:8082/events/E1/seats'
  When method GET
  Then status 200