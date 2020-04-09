package com.nanaten.SpringBootDemo.domain.repository

import com.nanaten.SpringBootDemo.domain.entity.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface ArticleRepository : JpaRepository<Article, Int> {
    @Transactional
    fun deleteByIdIn(ids: List<Int>)
    fun findAllByUserId(userId: Int, pageable: Pageable): Page<Article>
}