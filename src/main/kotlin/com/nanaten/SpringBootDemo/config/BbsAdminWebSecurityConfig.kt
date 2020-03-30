package com.nanaten.SpringBootDemo.config

import com.nanaten.SpringBootDemo.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class BbsAdminWebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(web: WebSecurity) {
        // ここに設定したものはセキュリティ設定を無視
        web.ignoring().antMatchers(
                "/favicon.ico", "/css/**", "/js/**"
        )
    }

    override fun configure(http: HttpSecurity) {
        // 許可の設定
        http.authorizeRequests()
                // /admin と/admin/配下は認証が必要
                .antMatchers("/admin", "/admin/*").authenticated().anyRequest().permitAll() // それ以外は全てアクセス許可
        // ログイン設定 (デフォルトの設定を用いる)
        http.formLogin()
        // ログアウト
        http.logout()
                .logoutRequestMatcher(AntPathRequestMatcher("/logout**")).logoutSuccessUrl("/")
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)
    }
}