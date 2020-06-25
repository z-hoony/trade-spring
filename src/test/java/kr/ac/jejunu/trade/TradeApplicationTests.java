package kr.ac.jejunu.trade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TradeApplicationTests {
    @Autowired
    MockMvc mvc;
    @MockBean
    private ArticleController articleController;
    @MockBean
    private UserController userController;

    private String title = "판매합니다";
    private String content = "연락주세요";
    private String name = "jade";
    private String password = "1234";
    private String nickname = "제이드";

    @Test
    public void articleList() throws Exception {
        List<Article> articles = new ArrayList<>();
        User user = User.builder().name(name).password(password).nickname(nickname).build();
        Article article = Article.builder().id(1).title(title).content(content).writer(user).writtenDate(1).build();
        articles.add(article);
        articles.add(article);
        given(articleController.list(0)).willReturn(articles);
        mvc.perform(get("/api/article/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", hasSize(lessThanOrEqualTo(20))))
                .andExpect(jsonPath("$[0].title", is(title)))
                .andExpect(jsonPath("$[0].writer.nickname", is(nickname)))
                .andExpect(jsonPath("$[0].content", is(content)))
                .andExpect(jsonPath("$[0].writtenDate", greaterThan(0)));
    }

    @Test
    public void getArticle() throws Exception {
        User user = User.builder().name(name).password(password).nickname(nickname).build();
        Article article = Article.builder().id(1).title(title).content(content).writer(user).build();

        given(articleController.get(1)).willReturn(article);
        mvc.perform(get("/api/article/detail/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.nickname", is(nickname)))
                .andExpect(jsonPath("$.content", is(content)))
                .andExpect(jsonPath("$.writtenDate", greaterThan(0)));
    }

    @Test
    public void createArticle() {

    }

    @Test
    public void deleteArticle() {

    }

    @Test
    public void editArticle() {

    }

    @Test
    public void changeArticleState() {

    }

    @Test
    public void commentList() {

    }

    @Test
    public void createComment() {

    }

    @Test
    public void deleteComment() {

    }

    @Test
    public void createUser() {

    }
}
