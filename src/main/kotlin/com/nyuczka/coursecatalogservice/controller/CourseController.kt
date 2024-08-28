package com.nyuczka.coursecatalogservice.controller

import com.nyuczka.coursecatalogservice.dto.CourseDTO
import com.nyuczka.coursecatalogservice.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCourse(@RequestBody course: CourseDTO): CourseDTO {
        return courseService.createCourse(course)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourses(): List<CourseDTO> {
        return courseService.getAllCourses()
    }

    @PutMapping
    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(@RequestBody course: CourseDTO, @PathVariable id: Int): CourseDTO {
        return courseService.updateCourse(course, id)
    }
}