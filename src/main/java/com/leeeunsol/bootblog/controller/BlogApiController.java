package com.leeeunsol.bootblog.controller;

import java.util.List;

import com.leeeunsol.bootblog.dto.UpdateArticleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.leeeunsol.bootblog.domain.Article;
import com.leeeunsol.bootblog.dto.AddArticleRequest;
import com.leeeunsol.bootblog.dto.ArticleResponse;
import com.leeeunsol.bootblog.service.BlogService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor 
@RestController // HTTP Response Body에 객체 데이터를 JSON으로 변환해주는 어노테이션
public class BlogApiController {
    
    private final BlogService blogService;

    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // @RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {

        Article saveArticle = blogService.save(request);
        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송 
        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
        
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll().stream().map(ArticleResponse::new).toList();

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request){
        Article updateArticle = blogService.update(id,request);

        return ResponseEntity.ok().body(updateArticle);
    }
}
