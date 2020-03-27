package com.nanaten.SpringBootDemo.service

import com.nanaten.SpringBootDemo.domain.entity.User
import com.nanaten.SpringBootDemo.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {

        val user: User? = username?.let { userRepository.findByName(it) }

        user ?: throw UsernameNotFoundException(username)

        return UserDetailsImpl(user)

    }
}