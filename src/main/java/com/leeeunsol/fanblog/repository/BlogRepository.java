package com.leeeunsol.fanblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leeeunsol.fanblog.domain.Article;


public interface BlogRepository extends JpaRepository<Article, Long> {
    
}
