package com.nyuczka.coursecatalogservice.service

import com.nyuczka.coursecatalogservice.dto.CourseDTO
import com.nyuczka.coursecatalogservice.entity.Course
import com.nyuczka.coursecatalogservice.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository) {

    companion object : KLogging()

    fun createCourse(course: CourseDTO): CourseDTO {
        val entity = course.let {
            Course(
                null,
                name = it.name,
                category = it.category
            )
        }

        courseRepository.save(entity)

        logger().info { "Course created: $entity" }

        return entity.let {
            CourseDTO(
                id = it.id,
                name = it.name,
                category = it.category
            )
        }
    }
}