package com.nyuczka.coursecatalogservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.nyuczka.coursecatalogservice.dto.InstructorDTO
import com.nyuczka.coursecatalogservice.service.InstructorService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(InstructorController::class)
class InstructorControllerTest {

    @MockkBean
    lateinit var instructorService: InstructorService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private val instructorDTO = InstructorDTO(1, "some name")


    @Test
    fun `should create an instructor`() {
        every { instructorService.createInstructor(any()) } returns instructorDTO

        mockMvc.perform(
            post("/v1/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(InstructorDTO(null, "some name")))
        )
            .andExpect(status().isCreated)
            .andExpect { jsonPath("$.name", "some name") }
            .andExpect { jsonPath("$.id").isNotEmpty }

    }
}