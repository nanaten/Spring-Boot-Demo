package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.Article
import com.nanaten.SpringBootDemo.domain.repository.ArticleRepository
import com.nanaten.SpringBootDemo.request.ArticleRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
class ArticleController {

    val MESSAGE_REGISTER_NORMAL = "正常に投稿できました"
    val MESSAGE = "message"
    val MESSAGE_ARTICLE_DOES_NOT_EXISTS = "対象の記事が見つかりませんでした。"
    val MESSAGE_UPDATE_NORMAL = "正常に更新しました。"
    val MESSAGE_ARTICLE_KEY_UNMATCH = "投稿 KEY が一致しません。"
    val ALERT_CLASS_ERROR = "alert-error"
    val ALERT_CLASS = "alert_class"
    val MESSAGE_DELETE_NORMAL = "正常に削除しました。"
    val ERRORS = "errors"
    val REQUEST = "request"
    val ARTICLE_REQUEST = "articleRequest"
    val PAGE_SIZE = 10

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @PostMapping("/")
    fun registerArticle(@Validated @ModelAttribute articleRequest: ArticleRequest,
                        result: BindingResult,
                        redirectAttributes: RedirectAttributes): String {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(ERRORS, result)
            redirectAttributes.addFlashAttribute(REQUEST, articleRequest)
            return "redirect:/"
        }
        val article = Article(
                articleRequest.id,
                articleRequest.name,
                articleRequest.title,
                articleRequest.contents,
                articleRequest.articleKey
        )
        articleRepository.save(article)

        redirectAttributes.addFlashAttribute(
                MESSAGE, MESSAGE_REGISTER_NORMAL
        )
        return "redirect:/"
    }

    @GetMapping("/")
    fun getArticleList(@ModelAttribute articleRequest: ArticleRequest,
                       @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                       model: Model): String {
        if (model.containsAttribute(ERRORS)) {
            val key = BindingResult.MODEL_KEY_PREFIX + ARTICLE_REQUEST
            model.addAttribute(key, model.asMap()[ERRORS])
        }
        if (model.containsAttribute(REQUEST)) {
            model.addAttribute(ARTICLE_REQUEST, model.asMap()[REQUEST])
        }

        val pageable = PageRequest.of(page, this.PAGE_SIZE, Sort.by(Sort.Direction.DESC, "updateAt").and(Sort.by(Sort.Direction.ASC, "id")))

        val articles = articleRepository.findAll(pageable)
        model.addAttribute("page", articles)
        return "index"
    }

    @GetMapping("/edit/{id}")
    fun getArticleEdit(@PathVariable id: Int, model: Model,
                       redirectAttributes: RedirectAttributes): String {
        return if (articleRepository.existsById(id)) {
            if (model.containsAttribute(REQUEST)) {
                model.addAttribute("article", model.asMap()[REQUEST])
            } else {
                model.addAttribute("article", articleRepository.findById(id))
            }
            if (model.containsAttribute(ERRORS)) {
                val key = BindingResult.MODEL_KEY_PREFIX + "article"
                model.addAttribute(key, model.asMap()[ERRORS])
            }

            "edit"
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            "redirect:/"
        }
    }

    @PostMapping("/update")
    fun updateArticle(@Validated articleRequest: ArticleRequest,
                      result: BindingResult,
                      redirectAttributes: RedirectAttributes): String {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(ERRORS, result)
            redirectAttributes.addFlashAttribute(REQUEST, articleRequest)

            return "redirect:/edit/${articleRequest.id}"
        }
        if (!articleRepository.existsById(articleRequest.id)) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article = articleRepository.findById(articleRequest.id).get()

        if (articleRequest.articleKey != article.articleKey) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_KEY_UNMATCH)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/edit/${articleRequest.id}"
        }

        article.name = articleRequest.name
        article.title = articleRequest.title
        article.contents = articleRequest.contents
        article.updateAt = Date()

        articleRepository.save(article)
        redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_UPDATE_NORMAL)
        return "redirect:/"
    }

    @GetMapping("/delete/confirm/{id}")
    fun getDeleteConfirm(@PathVariable id: Int, model: Model,
                         redirectAttributes: RedirectAttributes): String {
        if (!articleRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/"
        }
        model.addAttribute("article", articleRepository.findById(id).get())

        if (model.containsAttribute(ERRORS)) {
            val key = BindingResult.MODEL_KEY_PREFIX + "article"
            model.addAttribute(key, model.asMap()[ERRORS])
        }

        return "delete_confirm"
    }

    @PostMapping("/delete")
    fun deleteArticle(@Validated @ModelAttribute articleRequest: ArticleRequest,
                      result: BindingResult,
                      redirectAttributes: RedirectAttributes): String {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(ERRORS, result)
            redirectAttributes.addFlashAttribute(REQUEST, articleRequest)

            return "redirect:/delete/confirm/${articleRequest.id}"
        }

        if (!articleRepository.existsById(articleRequest.id)) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article = articleRepository.findById(articleRequest.id).get()
        if (articleRequest.articleKey != article.articleKey) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_KEY_UNMATCH)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/delete/confirm/${article.id}"
        }

        articleRepository.deleteById(articleRequest.id)

        redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_DELETE_NORMAL)
        return "redirect:/"
    }

    @GetMapping("/seed")
    fun addSeedData(): String {
        (1..50).forEach {
            val article = Article()
            article.name = "name$it"
            article.title = "title$it"
            article.contents = "contents$it"
            article.articleKey = "1"
            articleRepository.save(article)
        }
        return "redirect:/"
    }

}