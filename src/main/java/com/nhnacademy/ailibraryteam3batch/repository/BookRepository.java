package com.nhnacademy.ailibraryteam3batch.repository;

import com.nhnacademy.ailibraryteam3batch.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, CustomizedBookRepository {
}
