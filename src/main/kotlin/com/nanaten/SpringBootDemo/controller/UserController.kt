package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/login")
    fun getUserLogin(): String {
        return "user_login"
    }
}