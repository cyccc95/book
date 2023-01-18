package com.book.service;

import com.book.domain.Book;
import com.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // final 붙은 애로 자동으로 생성자 만들어줌 -> DI
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Transactional // 서비스 함수가 종료될 때 commit할지 rollback할지 트랜잭션 관리
    public Book 저장하기(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true) // JPA 변경감지라는 내부 기능 활성화X -> 내부 연산 줄임, update시의 정합성을 유지, insert의 유령데이터현상(팬텀현상) 못막음
        public Book 한건가져오기(Long id) {
        return bookRepository.findById(id).orElseThrow(()->
            new IllegalArgumentException("id를 확인해주세요")
        );
    }

    @Transactional(readOnly = true)
    public List<Book> 모두가져오기() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book 수정하기(Long id, Book book) {
        // 더티체킹 update
        Book bookEntity = bookRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("id를 확인해주세요")
        ); // 영속화
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        return bookEntity;
    } // 함수 종료 -> 트랜잭션 종료 -> 영속화 되어있는 데이터를 DB로 갱신(flush) -> commit : 더티체킹

    @Transactional
    public String 삭제하기(Long id) {
        bookRepository.deleteById(id); // 오류가 터지면 exception, 신경쓰지 말고
        return "ok";
    }
}
