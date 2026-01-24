package com.example.metricssample.repository

import io.kotest.core.spec.style.FunSpec
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.postgresql.PostgreSQLContainer

abstract class RepositorySpec(
    body: FunSpec.() -> Unit,
) : FunSpec(body) {
    companion object {
        private val db = PostgreSQLContainer("postgres:18.1")

        init {
            db.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDBContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", db::getJdbcUrl)
            registry.add("spring.datasource.username", db::getUsername)
            registry.add("spring.datasource.password", db::getPassword)
        }
    }
}
