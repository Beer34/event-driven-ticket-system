Feature: Payment Flow

Scenario: Payment success
  Given url 'http://localhost:8082/pay/A1'
  When method POST
  Then status 200

Scenario: Payment failure
  Given url 'http://localhost:8082/pay/A1'
  And param fail = true
  When method POST
  Then status 200