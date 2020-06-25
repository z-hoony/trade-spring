package kr.ac.jejunu.trade;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@Data
@AllArgsConstructor
class PageResult {
    private Boolean hasNext;
    private List<Article> articles;
}

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public PageResult list(@RequestParam("page") Integer pageNumber) {
        PageRequest request = PageRequest.of(pageNumber, 20, Sort.Direction.DESC, "id");
        Page page = articleRepository.findAll(request);
        return new PageResult(page.hasNext(), page.getContent());
    }

    @GetMapping("/{id}")
    public Article get(@PathVariable("id") Integer id) {
        return articleRepository.getOne(id);
    }

    @PostMapping
    public void write(@RequestParam(value = "image", required = false)MultipartFile image,
                      @RequestParam("title") String title,
                      @RequestParam("content") String content,
                      HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("loginUserId"));
        User loginUser = getUserFromSession(session);
        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Article article = Article.builder().title(title).content(content).writer(loginUser).build();
        articleRepository.save(article);

        if (image != null) {
            Integer articleId = article.getId();
            article.setImage(articleId + "_" + image.getOriginalFilename());
            articleRepository.save(article);

            File path = new File(request.getServletContext().getRealPath("/") + "/WEB-INF/static/" + articleId + "_" + image.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(image.getBytes());
            bufferedOutputStream.close();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer articleId, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loginUser = getUserFromSession(session);
        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Article article = articleRepository.getOne(articleId);
        if (article == null || article.getWriter().getId() != loginUser.getId()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (article.getImage() != null && article.getImage().isEmpty() == false) {
            File path = new File(request.getServletContext().getRealPath("/") + "/WEB-INF/static/" + article.getImage());
            if (path.exists()) {
                path.delete();
            }
        }
        articleRepository.delete(article);
    }

    private User getUserFromSession(HttpSession session) {
        Object userId = session.getAttribute("loginUserId");
        if (userId == null) {
            return null;
        }
        User loginUser = userRepository.getOne((Integer) userId);
        return loginUser;
    }
}
