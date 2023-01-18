package com.book.web;

// 통합 테스트 (모든 Bean들을 똑같이 IoC 올리고 테스트하는 것)

import com.book.domain.Book;
import com.book.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트에 필요 -> 모든 애들이 메모리에 다 뜸
 * WebEnvironment.MOCK : 실제 톰캣에 올리는게 아니라, 다른 톰캣으로 테스트
 * WebEnvironment.RANDOM_POR : 실제 톰캣으로 테스트
 * @AutoConfigureMockMvc : MockMvc를 IoC에 등록해줌
 * @Transactional : 각각의 테스트함수가 종료될 때마다 트랜잭션을 rollback해주는 어노테이션
 */

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init() {
//        List<Book> books = new ArrayList<>();
//        books.add(new Book(null, "스프링부트 따라하기", "코스"));
//        books.add(new Book(null, "리액트 따라하기", "코스"));
//        books.add(new Book(null, "Junit 따라하기", "코스"));
//        bookRepository.saveAll(books);
        entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
    }

//    @AfterEach
//    public void end() {
//        bookRepository.deleteAll();
//    }

    // BDDMockito 패턴 given, when, then
    @Test
    public void save_테스트() throws Exception {
        // given (테스트를 하기 위한 준비)
        Book book = new Book(null, "스프링 따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book); // json으로 바꿔줌
        log.info(content);
//        when(bookService.저장하기(book)).thenReturn(new Book(1L, "스프링 따라하기", "코스"));
        // 통합 테스트는 실제 Service가 뜨기 때문에 필요 없음
        // 실제로 sql문이 실행됨

        // when (테스트 실행)
        ResultActions resultActions = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then (검증)
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("스프링 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findAll_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리액트 따라하기", "코스"));
        books.add(new Book(null, "Junit 따라하기", "코스"));
//        when(bookService.모두가져오기()).thenReturn(books);
        bookRepository.saveAll(books);

        // when
        ResultActions resultActions = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_테스트() throws Exception {
        // given
        Long id = 2L;
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리액트 따라하기", "코스"));
        books.add(new Book(null, "Junit 따라하기", "코스"));
        bookRepository.saveAll(books);
//        when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "자바 공부하기", "쌀"));

        // when
        ResultActions resultActions = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("리액트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_테스트() throws Exception {
        // given
        Long id = 3L;
        Book book = new Book(null, "C++ 따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book); // json으로 바꿔줌

        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리액트 따라하기", "코스"));
        books.add(new Book(null, "Junit 따라하기", "코스"));
        bookRepository.saveAll(books);
//        when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "C++ 따라하기", "코스"));

        // when
        ResultActions resultActions = mockMvc.perform(put("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("C++ 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

}
