package com.example.rqchallenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RqChallengeApplicationTests {

    @Test
    void contextLoads() {
        assertEquals(5, 6, "2 + 3 should equal 5");

    }

}
