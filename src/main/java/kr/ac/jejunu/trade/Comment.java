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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Article article;
    @ManyToOne
    private User writer;
    private String content;
    private Long writtenDate;

    @PrePersist
    public void prePersist() {
        this.writtenDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
