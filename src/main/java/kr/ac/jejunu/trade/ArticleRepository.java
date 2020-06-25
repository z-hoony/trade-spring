package kr.ac.jejunu.trade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface ArticleInfo {
    Integer getId();
    UserInfo getWriter();
    String getTitle();
    String getContent();
    String getImage();
    Long getWrittenDate();
    ArticleState getState();
}

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Page<ArticleInfo> findArticlesBy(Pageable pageable);
    Optional<ArticleInfo> findArticleById(Integer id);
}
