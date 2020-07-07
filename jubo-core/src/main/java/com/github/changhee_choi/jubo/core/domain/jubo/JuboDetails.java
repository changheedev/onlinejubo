package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Changhee Choi
 * @since 14/07/2020
 */
@Data
public class JuboDetails {

    private Long id;
    private String title;
    private int viewCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<JuboContentDetails> contents = new ArrayList<>();

    @Builder
    private JuboDetails(Long id, String title, int viewCount, LocalDateTime startDate, LocalDateTime endDate,
                       List<JuboContentDetails> contents) {
        this.id = id;
        this.title = title;
        this.viewCount = viewCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contents = contents;
    }

    public static JuboDetails of(Jubo entity) {
        return JuboDetails.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .viewCount(entity.getViewCount())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .contents(entity.getContents().stream().map(JuboContentDetails::of).collect(Collectors.toList()))
                .build();
    }
}
