import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponseOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class DogAPIExample {
    String breed = "retriever";
    String subBreed = "golden";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://dog.ceo/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void apiRequestToProduceListOfDogBreeds() {
        // 1. Perform a API request to produce a list of all dog breeds
        String responseString =
                when().
                get("/breeds/list/all").
                then().
                        statusCode(200).
                        extract().
                        asString();
                        System.out.println("List of all Dog Breeds:-> "+responseString);
    }

    @Test
    public void verifyRetrieverBreedWithInList() {
        // 2. Using Code verify "retriever" breed is with in the breeds list.
        ValidatableResponseOptions vro =
                when().
                get("/breeds/list/all").
                then().
                    statusCode(200).
                    //verify "retriever" breed is with in the breeds list
                    body("message",hasKey(breed)).
                    body("message.retriever",containsInAnyOrder("chesapeake","flatcoated","curly","golden")).
                    body("message.retriever",contains("chesapeake","curly","flatcoated","golden"));
                //print whole response
                HashMap message = vro.extract().path("message");
                System.out.println("List of all Dog Breeds using HashMap:-> "+message);
                //print List of Retriever SubBreeds
                List<String> retriever = vro.extract().path("message.retriever");
                System.out.println("List of Retriever SubBreeds:-> "+retriever);
    }

    @Test
    public void apiRequestToProduceListOfRetrieverSubBreeds() {
        // Perform API request to produce a list of sub-breeds for "retriever".
        String responseString =
        given().
                pathParam("breed", breed).
        when().
                get("/breed/{breed}/list").
        then().
                statusCode(200).
                body("message", hasItem("golden")).
                extract().
                asString();
                System.out.println("List of Retriever SubBreeds:-> "+responseString);
    }

    @Test
    public void getRandomGoldenRetrieverImage(){
        // Get a random "golden retriever" image.
        String responseString =
        given().
                pathParam("breed",breed).
                pathParam("subbreed",subBreed).
        when().
                get("/breed/{breed}/{subbreed}/images/random").
        then().
                statusCode(200).
                body("message",startsWith("https://images.dog.ceo")).
                body("message",endsWith(".jpg")).
                extract().
                asString();
                System.out.println("Golden Retriever Image:-> "+responseString);
    }

}
