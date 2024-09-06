package com.nyuczka.coursecatalogservice.controller

import com.nyuczka.coursecatalogservice.dto.CourseDTO
import com.nyuczka.coursecatalogservice.entity.Course
import com.nyuczka.coursecatalogservice.repository.CourseRepository
import com.nyuczka.coursecatalogservice.repository.InstructorRepository
import com.nyuczka.coursecatalogservice.util.PostgreSQLContainerInitializer
import com.nyuczka.coursecatalogservice.util.courseEntityList
import com.nyuczka.coursecatalogservice.util.instructorEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import java.util.stream.Stream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseControllerIT : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    companion object {
        @JvmStatic
        fun nameAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("Spring", 2),
                Arguments.arguments("Wiremock", 1),
                Arguments.arguments("Zero", 0)
            )
        }
    }

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)
        courseRepository.saveAll(courseEntityList(instructor))
    }

    @Test
    fun `should create a new course`() {
        val instructor = instructorRepository.findAll().first()
        val courseDTO = CourseDTO(null, "Test Course", "Test Category", instructor.id)

        webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isNotEmpty
            .jsonPath("$.name").isEqualTo("Test Course")
            .jsonPath("$.category").isEqualTo("Test Category")
    }

    @Test
    fun `should get all courses`() {
        val coursesDTOs = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println(coursesDTOs)
        assertEquals(3, coursesDTOs!!.size)
    }


    @ParameterizedTest
    @MethodSource("nameAndSize")
    fun `should get all courses by name or return empty list`(name: String, size: Int) {
        val uri = UriComponentsBuilder
            .fromUriString("/v1/courses/")
            .queryParam("name", name)
            .toUriString()

        webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.size()")
            .isEqualTo(size)
    }

    @Test
    fun `should update the course`() {
        val first = instructorRepository.findAll().first()
        val course = Course(
            null,
            "Wiremock for Java Developers", "Development", first
        )

        val savedCourse = courseRepository.save(course)

        val courseDTO = CourseDTO(null, "Wiremock for Java Developers", "Testing", course.instructor!!.id)

        webTestClient.put()
            .uri("/v1/courses/${savedCourse.id}")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.name").isEqualTo("Wiremock for Java Developers")
            .jsonPath("$.category").isEqualTo("Testing")

    }

    @Test
    fun `should delete a course`() {
        webTestClient.delete()
            .uri("/v1/courses/1")
            .exchange()
            .expectStatus().isNoContent

        val existingCourses = courseRepository.findAll()

        assertEquals(existingCourses.count(), 2)

    }
}