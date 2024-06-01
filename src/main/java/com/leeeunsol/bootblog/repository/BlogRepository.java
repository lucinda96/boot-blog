package com.leeeunsol.bootblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leeeunsol.bootblog.domain.Article;


public interface BlogRepository extends JpaRepository<Article, Long> {
    
}
