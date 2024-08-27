package com.nyuczka.coursecatalogservice.repository

import com.nyuczka.coursecatalogservice.entity.Course
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface CourseRepository : CrudRepository<Course, Int> {
    fun findByName(name: String): Course?
    fun findByCategory(category: String): List<Course>
}