package com.nanaten.SpringBootDemo.repository

import com.nanaten.SpringBootDemo.entity.UserEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Int>