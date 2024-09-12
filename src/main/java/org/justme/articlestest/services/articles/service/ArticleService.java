package org.justme.articlestest.services.articles.service;

import org.justme.articlestest.data.entity.Article;
import org.justme.articlestest.data.repository.ArticleRepository;
import org.justme.articlestest.services.articles.dto.ArticleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Page<Article> getAllArticles(Pageable pageable) {

        return articleRepository.findAll(pageable);
    }

    public Map<LocalDate, Long> getArticleStatistics(LocalDate startDate, LocalDate endDate) {

        List<Article> articles = articleRepository.findByPublishDateBetween(startDate, endDate);

        return articles.stream()
                .collect(Collectors.groupingBy(Article::getPublishDate, Collectors.counting()));
    }

    public Article createArticle(ArticleDTO dto) {

        Article article = new Article();

        article.setTitle(dto.getTitle());
        article.setAuthor(dto.getAuthor());
        article.setContent(dto.getContent());
        article.setPublishDate(dto.getPublishDate());

        return articleRepository.save(article);
    }
}
