Feature: validate NACE list

  @cucumber
  Scenario: Application status end-points sucessfull
    Given I GET the details for NACE "398481"
    When search is executed successfully
    Then I validate the NACE details for "398481"

  @cucumber
  Scenario: Application status end-points fall
    Given I GET the details for NACE "398487"
    When search is executed unsuccessfully