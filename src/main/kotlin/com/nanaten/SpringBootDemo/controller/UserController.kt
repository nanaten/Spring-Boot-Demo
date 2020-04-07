package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.User
import com.nanaten.SpringBootDemo.domain.entity.UserRole
import com.nanaten.SpringBootDemo.domain.repository.UserRepository
import com.nanaten.SpringBootDemo.service.IUserDetailsService
import com.nanaten.SpringBootDemo.validator.UserValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    @Qualifier("userManagerServiceImpl")
    lateinit var userManagerService: IUserDetailsService

    @Autowired
    lateinit var userValidator: UserValidator

    @GetMapping("/login")
    fun getUserLogin(): String {
        return "user_login"
    }

    @GetMapping("/signup")
    fun getUserSignup(
            @ModelAttribute user: User,
            model: Model,
            redirectAttributes: RedirectAttributes
    ): String {
        model.addAttribute("user_role", UserRole.USER.name)

        if (model.containsAttribute("errors")) {
            val key = BindingResult.MODEL_KEY_PREFIX + "user"
            model.addAttribute(key, model.asMap()["errors"])
        }

        if (model.containsAttribute("request")) {
            model.addAttribute("user", model.asMap()["request"])
        }

        return "user_signup"
    }

    @PostMapping("/signup")
    fun postUserSignup(@Validated @ModelAttribute user: User,
                       result: BindingResult,
                       model: Model,
                       redirectAttributes: RedirectAttributes): String {
        userValidator.validate(user, result)
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result)
            redirectAttributes.addFlashAttribute("request", user)

            return "redirect:/user/signup"
        }

        userManagerService.registerUser(user, user.password)

        return "redirect:/user/login"
    }

    @GetMapping("/index")
    fun getUserIndex(): String {
        return "user_index"
    }

    @PostMapping("/login/auth")
    fun userLogin(): String {
        return "redirect:/user/index"
    }

    @GetMapping("/logout")
    fun getUserLogout(): String {
        return "redirect:/"
    }
}