package org.justme.articlestest.data.repository;

import org.justme.articlestest.data.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> findByPublishDateBetween(LocalDate after, LocalDate before);
}
