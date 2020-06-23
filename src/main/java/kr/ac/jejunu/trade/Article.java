package kr.ac.jejunu.trade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Integer id;
    private User writer;
    private String title;
    private String content;
    private String image;
    private Integer writtenDate;
}
