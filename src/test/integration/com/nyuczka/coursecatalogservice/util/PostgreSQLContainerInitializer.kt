package com.nyuczka.coursecatalogservice.util

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
open class PostgreSQLContainerInitializer {
    companion object {
        @Container
        val postgresDb = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:latest")).apply {
            withDatabaseName("testdb")
            withUsername("postgres")
            withPassword("secret")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresDb::getJdbcUrl)
            registry.add("spring.datasource.username", postgresDb::getUsername)
            registry.add("spring.datasource.password", postgresDb::getPassword)
        }
    }
}