package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.Article
import com.nanaten.SpringBootDemo.domain.entity.User
import com.nanaten.SpringBootDemo.domain.entity.UserRole
import com.nanaten.SpringBootDemo.domain.repository.ArticleRepository
import com.nanaten.SpringBootDemo.domain.repository.UserRepository
import com.nanaten.SpringBootDemo.request.ArticleRequest
import com.nanaten.SpringBootDemo.service.IUserDetailsService
import com.nanaten.SpringBootDemo.service.UserDetailsImpl
import com.nanaten.SpringBootDemo.validator.UserValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/user")
class UserController {
    val MESSAGE_REGISTER_NORMAL = "正常に投稿できました"
    val MESSAGE = "message"
    val ERRORS = "errors"
    val REQUEST = "request"

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    @Qualifier("userManagerServiceImpl")
    lateinit var userManagerService: IUserDetailsService

    @Autowired
    lateinit var userValidator: UserValidator
    val PAGE_SIZE: Int = 10

    @Autowired
    lateinit var articleRepository: ArticleRepository

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
    fun getUserIndex(@ModelAttribute atricleRequest: ArticleRequest,
                     @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
                     @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                     model: Model): String {
        model.addAttribute("user", userDetailsImpl.user)

        val pageable = PageRequest.of(
                page,
                this.PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "updateAt").and(Sort.by(Sort.Direction.ASC, "id"))
        )
        val articles = articleRepository.findAllByUserId(userDetailsImpl.user.id, pageable)
        model.addAttribute("page", articles)
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

    @PostMapping("/article/register")
    fun userArticleRegister(
            @Validated @ModelAttribute articleRequest: ArticleRequest,
            result: BindingResult,
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            redirectAttributes: RedirectAttributes): String {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result)
            redirectAttributes.addFlashAttribute("request", articleRequest)

            return "redirect:/user/index"
        }

        articleRepository.save(
                Article(
                        articleRequest.id,
                        articleRequest.name,
                        articleRequest.title,
                        articleRequest.contents,
                        articleRequest.articleKey,
                        Date(),
                        Date(),
                        userDetailsImpl.user.id
                )
        )
        redirectAttributes.addFlashAttribute("message", MESSAGE_REGISTER_NORMAL)

        return "redirect:/user/index"
    }
}