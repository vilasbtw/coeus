package com.coeus.api.integrationtests.controllers.withyaml;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.RefreshTokenDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.models.security.user.User;
import com.coeus.api.models.security.user.UserRole;
import com.coeus.api.repositories.security.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorizationControllerYamlTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private static TokenDTO tokenDTO;

    private static YAMLMapper objectMapper;

    @BeforeAll
    void setUp() {
        objectMapper = new YAMLMapper();

        if (userRepository.findByUsername("kvilas").isEmpty()) {
            var user = new User();

            user.setUsername("kvilas");
            user.setPassword(encoder.encode("123"));
            user.setRole(UserRole.COORDINATOR);

            userRepository.save(user);
        }
    }

    @Order(1)
    @Test
    void login() throws JsonProcessingException {
        AuthenticationDTO credentials = new AuthenticationDTO("kvilas", "123");

        tokenDTO = given().config(
                RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().
                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .basePath("auth/login")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(credentials, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, objectMapper);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Order(2)
    @Test
    void refreshToken() throws JsonProcessingException {
        RefreshTokenDTO request = new RefreshTokenDTO(tokenDTO.getRefreshToken());

        TokenDTO refreshedToken = given().config(
                RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().
                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .basePath("auth/refresh-token")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(request, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, objectMapper);

        assertNotNull(refreshedToken);
        assertNotNull(refreshedToken.getRefreshToken());
    }
}
