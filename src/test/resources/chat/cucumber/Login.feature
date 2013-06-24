Feature: Employee-only landing page options
  Employees are shown Employee Benefits link

Scenario: User lands on login page
    Given the user has browsed to homepage
    When the homepage loads
    Then the user sees the login form
    
    
Scenario: User logs in with valid detail
    Given the user has browsed to homepage
    When the user enters valid details
    Then the user is sent to the chat screen