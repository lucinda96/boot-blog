package com.leeeunsol.bootblog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.leeeunsol.bootblog.dto.UpdateArticleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leeeunsol.bootblog.domain.Article;
import com.leeeunsol.bootblog.dto.AddArticleRequest;
import com.leeeunsol.bootblog.repository.BlogRepository;

@SpringBootTest 
@AutoConfigureMockMvc
public class BlogApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper; 

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    void testAddArticle() throws Exception {
        
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "Content";
        final AddArticleRequest addArticleRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(addArticleRequest);

        //when
        ResultActions result = mockMvc.perform(post(url)
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(requestBody));

        
        //RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
        // .contentType(MediaType.APPLICATION_JSON_VALUE)
        // .content(requestBody);

        //     ResultActions result = mockMvc.perform(requestBuilder);
        
        //then
        result.andExpect(status().isCreated());

        
        // result.andExpect(MockMvcResultMatchers.status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
        
    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    void testFindAllArticles() throws Exception {
        
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value(title))            
                    .andExpect(jsonPath("$[0].content").value(content));
                    

    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    void testFindArticle() throws Exception {
        
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url,savedArticle.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }


    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception{

        //given
        final String url = "/api/articles/{id}";
        final String title = "deleteTitle";
        final String content = "deleteContent";

        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        mockMvc.perform(delete(url,savedArticle.getId())).andExpect(status().isOk());

        // then
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {

        // given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());
        final String newTitle = "new Title";
        final String newContent = "new Content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle,newContent);

        //when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}
