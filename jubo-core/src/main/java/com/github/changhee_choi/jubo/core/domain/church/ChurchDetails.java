package com.github.changhee_choi.jubo.core.domain.church;

import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@Data
public class ChurchDetails {
    private UUID id;
    private String name;
    private int memberNum;
    private List<JuboDetails> juboList;

    @Builder
    private ChurchDetails(UUID id, String name, int memberNum, List<JuboDetails> juboList) {
        this.id = id;
        this.name = name;
        this.memberNum = memberNum;
        this.juboList = juboList;
    }

    public static ChurchDetails of(Church entity) {
        return ChurchDetails.builder()
                .id(entity.getId())
                .name(entity.getName())
                .memberNum(entity.getMemberNum())
                .juboList(entity.getJuboList().stream().map(JuboDetails::of).collect(Collectors.toList()))
                .build();
    }
}
