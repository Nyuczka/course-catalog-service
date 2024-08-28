package com.nyuczka.coursecatalogservice.service

import com.nyuczka.coursecatalogservice.dto.CourseDTO
import com.nyuczka.coursecatalogservice.entity.Course
import com.nyuczka.coursecatalogservice.exception.CourseNotFoundException
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

    fun getAllCourses(): List<CourseDTO> {
        return courseRepository.findAll().map {
            CourseDTO(
                id = it.id,
                name = it.name,
                category = it.category
            )
        }
    }

    fun updateCourse(course: CourseDTO, id: Int): CourseDTO {
        val entity =
            courseRepository.findById(id)

        if (entity.isPresent) {
            entity.get().let {
                it.name = course.name
                it.category = course.category
                courseRepository.save(it)
                return CourseDTO(
                    id = it.id,
                    name = it.name,
                    category = it.category
                )
            }
        } else {
            throw CourseNotFoundException("Course not found")
        }

    }
}