package com.literalura.Literalura.repository;

import com.literalura.Literalura.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByTitleContaining(String title);
    Long countByLanguage(String language);

    @Query("SELECT DISTINCT b.language FROM BookEntity b")
    List<String> findDistinctLanguages();

    @Query("SELECT b FROM BookEntity b ORDER BY b.downloads DESC LIMIT 10")
    List<BookEntity> findTop10ByOrderByDownloadsDesc();
}