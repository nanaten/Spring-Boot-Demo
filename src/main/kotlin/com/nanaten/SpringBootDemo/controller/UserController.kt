package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.User
import com.nanaten.SpringBootDemo.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    @PostMapping("/add")
    fun addNewUser(@RequestParam name: String): String {
        userRepository.save(User(name = name))
        return "Save Success."
    }

    @GetMapping("/all")
    fun getAllUsers(): Iterable<User>? {
        return userRepository.findAll()
    }

    @PostMapping("/update")
    fun updateUser(@RequestParam id: Int, name: String): String {
        userRepository.save(User(id, name))
        return "Update Success."
    }

    @PostMapping("/delete")
    fun deleteUser(@RequestParam id: Int): String {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            "Delete Success."
        } else {
            "User ID Unavailable."
        }
    }
}