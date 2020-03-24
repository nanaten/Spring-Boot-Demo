package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.Article
import com.nanaten.SpringBootDemo.domain.repository.ArticleRepository
import com.nanaten.SpringBootDemo.request.ArticleRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ArticleController {
    @Autowired
    lateinit var articleRepository: ArticleRepository

    @PostMapping("/")
    fun registerArticle(@ModelAttribute articleRequest: ArticleRequest): String {
        val article = Article(
                articleRequest.id,
                articleRequest.name,
                articleRequest.title,
                articleRequest.contents,
                articleRequest.articleKey
        )
        articleRepository.save(article)
        return "redirect:/"
    }

    @GetMapping("/")
    fun getArticleList(model: Model): String {
        model.addAttribute("articles", articleRepository.findAll())
        return "index"
    }

    @GetMapping("/edit/{id}")
    fun getArticleEdit(@PathVariable id: Int, model: Model): String {
        return if (articleRepository.existsById(id)) {
            model.addAttribute("article", articleRepository.findById(id))
            "edit"
        } else {
            "redirect:/"
        }
    }
}