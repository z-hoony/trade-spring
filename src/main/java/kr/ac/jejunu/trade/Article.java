package kr.ac.jejunu.trade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    @PrePersist
    public void writtenDate() {
        this.writtenDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
