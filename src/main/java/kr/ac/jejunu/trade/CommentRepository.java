package kr.ac.jejunu.trade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

interface UserInfo {
    String getNickname();
}

interface CommentInfo {
    Integer getId();
    UserInfo getWriter();
    String getContent();
    Long getWrittenDate();
}

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<CommentInfo> findCommentsByArticle(Article article);
}
