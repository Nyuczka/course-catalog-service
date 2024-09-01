package com.nyuczka.coursecatalogservice.repository

import com.nyuczka.coursecatalogservice.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface CourseRepository : CrudRepository<Course, Int> {
    fun findByNameContaining(name: String): List<Course>

    @Query(value = "SELECT * FROM COURSES WHERE NAME LIKE %?1%", nativeQuery = true)
    fun findCoursesByName(name: String): List<Course>
    fun findByCategory(category: String): List<Course>
}