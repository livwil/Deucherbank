Feature: validate NACE list

  @cucumber
  Scenario: Application status end-points
    Given I GET the details for NACE "398481"
    When search is executed successfully
    Then I validate the NACE details for "398481"