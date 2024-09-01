package com.nyuczka.coursecatalogservice.service

import com.nyuczka.coursecatalogservice.dto.InstructorDTO
import com.nyuczka.coursecatalogservice.entity.Instructor
import com.nyuczka.coursecatalogservice.repository.InstructorRepository
import org.springframework.stereotype.Service

@Service
class InstructorService(val instructorRepository: InstructorRepository) {

    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructorEntity = Instructor(
            null,
            name = instructorDTO.name
        )

        instructorRepository.save(instructorEntity)

        return instructorEntity.let {
            InstructorDTO(
                id = it.id,
                name = it.name
            )
        }
    }

}
