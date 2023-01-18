package com.book.repository;

import com.book.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 단위 테스트 (DB 관련된 Bean이 IoC에 등록되면 됨)

//Failed to replace DataSource with an embedded database for tests 에러 해결하기 위해 Replace.NONE 으로 변경
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 가짜 DB로 테스트, Replace.NONE : 실제 DB로 테스트
@DataJpaTest // @DataJpaTest가 Repository들을 다 IoC 등록해줌 -> @Mock X
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void save_테스트() {
        // given
        Book book = new Book(null, "책제목1", "책저자1");

        // when
        Book bookEntity = bookRepository.save(book);

        // then
        assertEquals("책제목1", bookEntity.getTitle());
    }
}
