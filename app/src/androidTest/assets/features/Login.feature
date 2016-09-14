Feature: Login
  Scenario: Login Success
    Given User specify username "test@test.com"
    And User specify password "12345678"
    When User press login button
    Then Welcome text "Welcome test@test.com" is displayed