package kr.ac.jejunu.trade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

enum ArticleState {
    WAIT,
    TRADE,
    COMPLETE;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User writer;
    private String title;
    private String content;
    private String image;
    private Long writtenDate;
    @Enumerated(EnumType.ORDINAL)
    private ArticleState state;

    @PrePersist
    public void prePersist() {
        this.writtenDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.state = ArticleState.WAIT;
    }
}
