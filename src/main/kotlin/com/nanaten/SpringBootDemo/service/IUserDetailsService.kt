package com.nanaten.SpringBootDemo.service

import com.nanaten.SpringBootDemo.domain.entity.User

interface IUserDetailsService {
    fun registerUser(user: User, rawPassword: String)
}
