package com.nanaten.SpringBootDemo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class BbsAdminWebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {

        auth?.inMemoryAuthentication()
                ?.withUser("admin")
                ?.password("\$2a\$10\$CPNJ.PlWH8k1aMhC6ytjIuwxYuLWKMXTP3H6h.LRnpumtccpvXEGy")
                ?.authorities("ROLE_ADMIN")

    }
}