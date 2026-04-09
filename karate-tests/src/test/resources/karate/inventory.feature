Feature: Inventory Service API

Scenario: Get seats
  Given url 'http://localhost:8081/seats/E1'
  When method GET
  Then status 200

Scenario: Reserve seat
  Given url 'http://localhost:8081/reserve/A1'
  When method GET
  Then status 200