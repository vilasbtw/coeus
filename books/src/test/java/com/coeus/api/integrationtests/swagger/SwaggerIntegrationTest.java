package com.coeus.api.integrationtests.swagger;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void shouldDisplaySwaggerUIPage() {

        // using rest assured to check if Swagger index page is being rendered.
        var content = given()
      			.basePath("/swagger-ui/index.html")
      				.port(TestConfigs.SERVER_PORT)
      			.when()
      				.get()
      			.then()
      				.statusCode(200)
      			.extract()
      				.body()
      					.asString();
      		assertTrue(content.contains("Swagger UI"));
    }

}
