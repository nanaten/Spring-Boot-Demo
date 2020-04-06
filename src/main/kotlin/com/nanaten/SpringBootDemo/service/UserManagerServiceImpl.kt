package com.nanaten.SpringBootDemo.service

import com.nanaten.SpringBootDemo.domain.entity.User
import com.nanaten.SpringBootDemo.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service


@Service
@Component
class UserManagerServiceImpl : IUserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun registerUser(user: User, rawPassword: String) {
        user.password = passwordEncoder.encode(rawPassword)
        userRepository.save(user)
    }
}