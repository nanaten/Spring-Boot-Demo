package com.nanaten.SpringBootDemo.config

import com.nanaten.SpringBootDemo.domain.entity.UserRole
import com.nanaten.SpringBootDemo.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@Order(1)
class BbsAdminWebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    override fun configure(http: HttpSecurity) {
        // 許可の設定
        http
                .antMatcher("/admin/**")
                .authorizeRequests()
                .antMatchers("/admin/login").permitAll()
                .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name)
                .anyRequest().authenticated()

        // ログイン設定
        http.formLogin()
                .loginProcessingUrl("/admin/login/auth")
                .loginPage("/admin/login")

        // ログアウト
        http.logout()
                .logoutRequestMatcher(AntPathRequestMatcher("/admin/logout"))
                .logoutSuccessUrl("/")
    }
}