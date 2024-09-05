package com.nyuczka.coursecatalogservice.repository

import com.nyuczka.coursecatalogservice.entity.Instructor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface InstructorRepository : CrudRepository<Instructor, Int> {

}
