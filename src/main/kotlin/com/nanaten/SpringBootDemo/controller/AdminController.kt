package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AdminController {

    val PAGE_SIZE: Int = 10

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @GetMapping("/admin/index")
    fun getAdminIndex(@RequestParam(value = "page", defaultValue = "0", required = false) page: Int, model: Model): String {
        val pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "updateAt").and(Sort.by(Sort.Direction.ASC, "id")))

        val articles = articleRepository.findAll(pageable)
        model.addAttribute("page", articles)
        return "admin_index"
    }
}