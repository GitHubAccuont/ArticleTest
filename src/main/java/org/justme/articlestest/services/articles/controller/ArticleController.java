package org.justme.articlestest.services.articles.controller;

import jakarta.validation.Valid;
import org.justme.articlestest.data.entity.Article;
import org.justme.articlestest.services.articles.dto.ArticleDTO;
import org.justme.articlestest.services.articles.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
@Validated
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {

        return ResponseEntity.ok(articleService.createArticle(articleDTO));
    }

    @GetMapping
    public ResponseEntity<Page<Article>> getAllArticles(Pageable pageable) {
        return ResponseEntity.ok(articleService.getAllArticles(pageable));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('PRIVILEGE_WATCH_STATISTICS')")
    public ResponseEntity<Map<LocalDate, Long>> getArticleStatistics() {
        LocalDate today = LocalDate.now();

        Map<LocalDate, Long> statistics = articleService.getArticleStatistics(today.minusWeeks(1), today);
        return ResponseEntity.ok(statistics);
    }
}
