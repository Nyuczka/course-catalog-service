package com.nyuczka.coursecatalogservice.repository

import com.nyuczka.coursecatalogservice.util.courseEntityList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIT {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        courseRepository.saveAll(courseEntityList())
    }

    @Test
    fun `should find courses by name containing`() {
        val courses = courseRepository.findByNameContaining("Spring")

        println(courses)

        assertEquals(2, courses.size)
    }

    @Test
    fun `should find courses by name`() {
        val courses = courseRepository.findCoursesByName("Spring")

        println(courses)

        assertEquals(2, courses.size)
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun `should find courses by name parametrized`(name: String, expectedSize: Int) {
        val courses = courseRepository.findCoursesByName(name)

        println(courses)

        assertEquals(expectedSize, courses.size)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(Arguments.arguments("Spring", 2), Arguments.arguments("Wiremock", 1))
        }
    }

}