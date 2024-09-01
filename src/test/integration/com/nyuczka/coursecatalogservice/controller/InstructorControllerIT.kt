package com.nyuczka.coursecatalogservice.controller

import com.nyuczka.coursecatalogservice.dto.InstructorDTO
import com.nyuczka.coursecatalogservice.repository.InstructorRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class InstructorControllerIT {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var instructorRepository: InstructorRepository


    @BeforeEach
    fun setUp() {
        instructorRepository.deleteAll()
    }

    @Test
    fun `should create an instructor`() {
        val instructorDTO = InstructorDTO(null, "Test name")

        webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isNotEmpty
    }


    @Test
    fun `should do bean validation`() {
        webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(InstructorDTO(null, ""))
            .exchange()
            .expectStatus().isBadRequest
    }
}