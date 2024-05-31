package com.leeeunsol.fanblog.service;

import java.util.List;

import com.leeeunsol.fanblog.dto.UpdateArticleRequest;
import org.springframework.stereotype.Service;

import com.leeeunsol.fanblog.domain.Article;
import com.leeeunsol.fanblog.dto.AddArticleRequest;
import com.leeeunsol.fanblog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service //빈으로 등록
public class BlogService {
    
    private final BlogRepository blogRepository;

    // 블로그 글 추가 메소드
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    // 블로그 글 전체 조회
    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    //블로그 글 아이디로 조회
    public Article findById(long id){
        //검색결과가 없을경우 IllegalAnnotationException을 thorw할것(람다식으로 작성된 문장)
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    // 블로그 글 삭제
    public void delete(long id){
        blogRepository.deleteById(id);
    }

    // 블로그 글 수정
    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(),request.getContent());

        return article;
    }
}
