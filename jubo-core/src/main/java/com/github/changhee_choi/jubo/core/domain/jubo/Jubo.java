package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@Entity
@Table(name = "oj_jubo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"title", "startDate"}, callSuper = false)
@ToString(callSuper = true)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;

    @OneToMany(mappedBy = "jubo", cascade = CascadeType.ALL)
    private List<JuboContent> contents = new ArrayList<>();

    @Builder
    public Jubo(String title, LocalDateTime startDate, Church church) {
        this.title = title;
        this.viewCount = 0;
        updateStartDate(startDate);
        this.church = church;
        this.church.addJubo(this);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        this.endDate = startDate.plusDays(6);
    }
}
