package com.ankush.cleancity;

import com.ankush.cleancity.Query.ComplaintQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CleancityApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/java/api/test/ping",
                String.class)).contains("PONG");
    }

    @Test
    void testComplaintQuery() {
        ComplaintQuery query = new ComplaintQuery();
        query.setLocation("pune");
        query.setStatus(List.of("COMPLETE","PENDING"));
        query.setSeverity(List.of("COMPLETE","PENDING"));
        log.info(query.compile().toString());
    }
}
