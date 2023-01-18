package com.book.service;

import com.book.domain.Book;
import com.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 *  단위 테스트 (Service 관련된 애들만 메모리에 띄우면 됨)
 *  BoardReposity : 가짜 객체로 만들 수 있음 -> 그 환경을 MockitoExtension이 제공
 *
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks // BookService 객체가 만들어질 때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 애들을 주입받는다
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void 저장히기_테스트() {
        // given
        Book book = new Book();
        book.setTitle("책제목1");
        book.setAuthor("책저자1");

        // stub
        when(bookRepository.save(book)).thenReturn(book);

        // test execute
        Book bookEntity = bookService.저장하기(book);

        // then
        assertEquals(bookEntity, book);
    }
}
