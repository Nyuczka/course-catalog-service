package com.nyuczka.coursecatalogservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    val name: String,
    val category: String
)
