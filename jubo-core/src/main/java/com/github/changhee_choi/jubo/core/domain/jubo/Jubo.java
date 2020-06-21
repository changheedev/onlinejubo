package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@Entity
@Table(name = "oj_jubo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jubo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    public Jubo(String title, LocalDateTime startDate) {
        this.title = title;
        this.viewCount = 0;
        updateStartDate(startDate);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        this.endDate = startDate.plusDays(6);
    }
}
