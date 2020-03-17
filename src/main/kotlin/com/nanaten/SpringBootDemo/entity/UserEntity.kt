package com.nanaten.SpringBootDemo.entity

import javax.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = 0,
        @Column(name = "name")
        val name: String
)