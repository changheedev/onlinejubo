package com.github.changhee_choi.jubo.core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@Data
@Builder
public class ChurchDTO {
    private UUID id;
    private String name;
    private int memberNum;
}
