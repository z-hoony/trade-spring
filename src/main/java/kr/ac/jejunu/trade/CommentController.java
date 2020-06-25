package kr.ac.jejunu.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/{id}/list")
    public List<CommentInfo> list(@PathVariable("id") Integer articleId, HttpServletResponse response) {
        try {
            Article article = articleRepository.findById(articleId).get();
            List<CommentInfo> comments = commentRepository.findCommentsByArticle(article);
            return comments;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @PostMapping("/{id}")
    public CommentInfo write(@PathVariable("id") Integer articleId, @RequestParam("comment") String content, HttpSession session, HttpServletResponse response) {
        User loginUser = getUserFromSession(session);
        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        try {
            Article article = articleRepository.findById(articleId).get();
            if (article.getState() == ArticleState.COMPLETE) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
            Comment comment = Comment.builder().article(article).writer(loginUser).content(content).build();
            commentRepository.save(comment);
            return commentRepository.findCommentById(comment.getId()).get();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    @DeleteMapping("/{articleId}/{commentId}")
    public void delete(@PathVariable("articleId") Integer articleId, @PathVariable("commentId") Integer commentId, HttpSession session, HttpServletResponse response) {
        User loginUser = getUserFromSession(session);
        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            Comment comment = commentRepository.findById(commentId).get();
            Article article = comment.getArticle();
            if (article.getId() != articleId || article.getState() == ArticleState.COMPLETE) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            if (comment.getWriter().getId() == loginUser.getId()) {
                commentRepository.delete(comment);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private User getUserFromSession(HttpSession session) {
        Object userId = session.getAttribute("loginUserId");
        if (userId == null) {
            return null;
        }
        return userRepository.getOne((Integer) userId);
    }
}
