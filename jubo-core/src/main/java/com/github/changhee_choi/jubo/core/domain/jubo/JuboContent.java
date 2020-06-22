package com.github.changhee_choi.jubo.core.domain.jubo;

import lombok.*;

import javax.persistence.*;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@Entity
@Table(name = "oj_jubo_content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class JuboContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    public JuboContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
