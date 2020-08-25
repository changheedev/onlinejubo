package com.github.changhee_choi.jubo.manager;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 23/07/2020
 */
public class TestParameterSupport {
    protected final UUID churchId = UUID.fromString("dbe07414-49d1-11e6-b7a7-0242ac140002");

    protected final Long juboId = 1L;

    protected final List<UUID> attachmentIds = Arrays.asList(
            UUID.fromString("a0fd7051-c82e-11ea-a901-0242ac120003"),
            UUID.fromString("9d6533ac-c83b-11ea-a901-0242ac120003"));

    protected final String timetableTypeContentSample = "[" +
            "{\\\"label\\\" : \\\"묵도\\\", \\\"value\\\" : \\\"시 65:1~4\\\"}, " +
            "{\\\"label\\\" : \\\"찬송\\\", \\\"value\\\" : \\\"1장\\\"}, " +
            "{\\\"label\\\" : \\\"신앙고백\\\", \\\"value\\\" : \\\"사도행전\\\"}" +
            "]";

    protected final String postTypeContentSample = "<p>교회소식</p><br>" +
            "<p><img src='https://example.com/images/test1.jpg'/></p>";
}
