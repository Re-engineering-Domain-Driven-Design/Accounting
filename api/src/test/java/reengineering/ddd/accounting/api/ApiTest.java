package reengineering.ddd.accounting.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import reengineering.ddd.accounting.api.config.TestApplication;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestApplication.class)
public class ApiTest {
    @Value("${local.server.port}")
    private int port;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @BeforeEach
    public void before() {
        RestAssured.port = port;
        RestAssured.basePath = contextPath;
    }
}

