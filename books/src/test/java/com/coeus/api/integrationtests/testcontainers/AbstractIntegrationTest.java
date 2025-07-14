package com.coeus.api.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

// this class job is to create the dynamical mysql container.

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    /*
    // this class is static to ensure a single container instance during software execution.
    // there are simpler ways to implement it, but they allow multiple container instances at runtime,
    // which can compromise the performance.
    */
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.1.0");

        // startContainter() will use the MySQLContainer **DEFAULT** database name, username and password.
        // the only parameter we'll specify is the MySQL version via constructor.
        private static void startContainers() {
            // starts containers in parallel and blocks until they are fully initialized.
            Startables.deepStart(Stream.of(mysql)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword(),
                    "server.port", "8888"
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();

            // gets environment properties from the application context and application.properties.
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            // injects properties (url, username, password) into the Spring environment.
            MapPropertySource testcontainers = new MapPropertySource("testcontainers",
                    (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}