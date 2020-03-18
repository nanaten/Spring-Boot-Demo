package com.nanaten.SpringBootDemo.domain.repository

import com.nanaten.SpringBootDemo.domain.entity.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Int>