package org.dbank;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;

import static io.restassured.RestAssured.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
public class StepDefinitions {
    private ClientAndServer mockServer;
    public Response response;
    @Before
    public  void setupClass()  {
        System.out.println("Starting server");
        mockServer = startClientAndServer(1080);
        createExpectations();
    }

    private void createExpectations() {
        String content = getContent();
        if(mockServer.hasStarted()){

            mockServer.when(request().withPath("/orders")
                    .withQueryStringParameter("order","398481"))
                    .respond(response().withBody(content,MediaType.APPLICATION_JSON));
        }
    }

    @Given("I GET the details for NACE {string}")
    public void iGETTheDetailsForNACE(String arg0) {
        response = get("http://localhost:1080/orders?order="+arg0).andReturn();

    }

    @When("search is executed successfully")
    public void searchIsExecutedSuccessfully() {
        assertThat(response.getStatusCode(),equalTo(200));
    }

    @Then("I validate the NACE details for {string}")
    public void iValidateTheNACEDetailsFor(String arg0) {
        JsonPath jsonPath =  response.body().jsonPath();
        assertThat(jsonPath.get("Order"),equalTo(arg0));
        assertThat(jsonPath.get("Level"),equalTo("1"));
        assertThat(jsonPath.get("Code"),equalTo("A"));
        assertThat(jsonPath.get("Parent"),equalTo(""));
        assertThat(jsonPath.get("Description"),equalTo("AGRICULTURE, FORESTRY AND FISHING"));
    }

    private String getContent() {
        return "{\n" +
                "    \"Order\": \"398481\",\n" +
                "    \"Level\": \"1\",\n" +
                "    \"Code\": \"A\",\n" +
                "    \"Parent\": \"\",\n" +
                "    \"Description\": \"AGRICULTURE, FORESTRY AND FISHING\",\n" +
                "    \"This item includes\": \"This section includes the exploitation of vegetal and animal natural resources,comprising the activities of growing of crops, raising and breeding of animals,harvesting of timber and other plants,animals or animal products from a farm or their natural habitats.\",\n" +
                "    \"This item also includes\":\"\",\n" +
                "    \"Rulings\":\"\",\n" +
                "    \"This item excludes\":\"\",\n" +
                "    \"Reference to ISIC Rev. 4\": \"A\"\n" +
                "  }";
    }
    @After
    public void tearDown(){
        System.out.println("Stopping server");
        mockServer.stop();
    }
}
