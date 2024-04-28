package com.example.metricssample

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class MetricsSampleApplicationTests : DbTest() {
    @Test
    fun contextLoads() {
    }
}
