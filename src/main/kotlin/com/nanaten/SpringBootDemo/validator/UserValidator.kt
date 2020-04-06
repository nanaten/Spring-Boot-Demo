package com.nanaten.SpringBootDemo.validator

import com.nanaten.SpringBootDemo.domain.entity.User
import com.nanaten.SpringBootDemo.domain.entity.UserRole
import com.nanaten.SpringBootDemo.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator


@Component
class UserValidator : Validator {
    val PASSWORD_LENGTH_MIN = 4
    val PASSWORD_LENGTH_MAX = 8

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var messageSource: MessageSource

    override fun validate(target: Any, errors: Errors) {
        if (target !is User) {
            errors.reject("TARGET_IS_NOT_USER_OBJECT")
            return
        }

        // Emailアドレスの重複チェック
        if (userRepository.findByEmail(target.email) != null) {
            errors.rejectValue("email", "REGISTERED_EMAIL")
        }

        // パスワードの長さチェック
        val passwordLengthRange = PASSWORD_LENGTH_MIN..PASSWORD_LENGTH_MAX
        if (target.password.length !in passwordLengthRange) {
            errors.rejectValue("password", "PASSWORD_LENGTH_ERROR", arrayOf(PASSWORD_LENGTH_MIN, PASSWORD_LENGTH_MAX), null)
        }

        // パスワード使用文字のチェック
        if (!target.password.matches(Regex("[a-zA-Z0-9\\-!_]*"))) {
            errors.rejectValue("password", "PASSWORD_ILLEGAL_CHAR")
        }

        // ロールのチェック
        if (target.role != UserRole.USER) {
            errors.rejectValue("role", "NOT_USER_ROLE")
        }
    }

    override fun supports(clazz: Class<*>): Boolean {
        return User::class == clazz
    }
}
