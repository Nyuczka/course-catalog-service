package com.nyuczka.coursecatalogservice.controller

import com.nyuczka.coursecatalogservice.dto.CourseDTO
import com.nyuczka.coursecatalogservice.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCourse(@RequestBody @Valid course: CourseDTO): CourseDTO {
        return courseService.createCourse(course)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourses(): List<CourseDTO> {
        return courseService.getAllCourses()
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getCoursesByName(@RequestParam(required = false) name: String?): List<CourseDTO> {
        return courseService.getCoursesByName(name)
    }

    @PutMapping
    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(@RequestBody course: CourseDTO, @PathVariable id: Int): CourseDTO {
        return courseService.updateCourse(course, id)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable id: Int) {
        courseService.deleteCourse(id)
    }
}