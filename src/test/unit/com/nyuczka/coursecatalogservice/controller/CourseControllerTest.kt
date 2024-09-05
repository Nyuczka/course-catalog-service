package com.nyuczka.coursecatalogservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.nyuczka.coursecatalogservice.dto.CourseDTO
import com.nyuczka.coursecatalogservice.service.CourseService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CourseController::class)
class CourseControllerTest {


    @MockkBean
    lateinit var courseService: CourseService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private val courseDTO = CourseDTO(1, "New Course", "Development", 1)


    @Test
    fun `should create a course`() {
        every { courseService.createCourse(any()) } returns courseDTO

        val response = mockMvc.perform(
            post(
                "/v1/courses",
                CourseDTO(null, "New Course", "Development")
            )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO))
        )


        response.andExpect(status().isCreated)
            .andExpect { jsonPath("$.name", courseDTO.name) }
            .andExpect { jsonPath("$.category", courseDTO.category) }
    }

    @Test
    fun `should handle bean validation`() {
        val course = CourseDTO(null, "", "")

        every { courseService.createCourse(any()) } returns CourseDTO(1, "", "")

        val response = mockMvc.perform(
            post("/v1/courses", course)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course))
        )

        response.andExpect(status().isBadRequest)
    }

    @Test
    fun `should handle runtime exception`() {

        every { courseService.createCourse(any()) } throws RuntimeException("Unexpected error")

        val response = mockMvc.perform(
            post("/v1/courses", courseDTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO))
        )

        response.andExpect(status().isInternalServerError)
    }

    @Test
    fun `should get list of courses`() {
        every { courseService.getAllCourses() } returns listOf(courseDTO)

        val response = mockMvc.perform(get("/v1/courses"))

        response
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.size())", 1) }
    }

    @Test
    fun `should update an existing course`() {
        val updatedCourse = CourseDTO(1, "New Course", "Some other category")
        every { courseService.updateCourse(any(), any()) } returns updatedCourse

        val response = mockMvc.perform(
            put("/v1/courses/{id}", 1, updatedCourse)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCourse))
        )

        response
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.category", updatedCourse.category) }
    }

    @Test
    fun `should delete course`() {
        every { courseService.deleteCourse(any()) } returns Unit
        val response = mockMvc.perform(delete("/v1/courses/{id}", 1))

        response
            .andExpect(status().isNoContent)
    }


}