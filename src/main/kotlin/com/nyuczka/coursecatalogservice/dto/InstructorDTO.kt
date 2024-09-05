package com.nyuczka.coursecatalogservice.dto

import jakarta.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,
    @get:NotBlank(message = "instructorDTO.name cannot be blank")
    var name: String
)
