package kr.ac.jejunu.trade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

@DataJpaTest
public class ArticleRepositoryTests {
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void findAll() {
        String title = "판매합니다";
        Article article = Article.builder().title(title).build();
        entityManager.persist(article);
        PageRequest request = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
        List<Article> articles = articleRepository.findAll(request).getContent();
        assertThat(articles.get(0).getId(), greaterThan(0));
        assertThat(articles.get(0).getTitle(), is(title));
    }
}
