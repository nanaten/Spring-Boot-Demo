package com.nanaten.SpringBootDemo.domain.repository

import com.nanaten.SpringBootDemo.domain.entity.UserEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Int>