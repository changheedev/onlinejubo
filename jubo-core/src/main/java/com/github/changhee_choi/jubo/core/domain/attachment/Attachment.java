package com.github.changhee_choi.jubo.core.domain.attachment;

import com.github.changhee_choi.jubo.core.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 22/06/2020
 */
@Entity
@Table(name = "oj_attachment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Builder
    public Attachment(String path, String originName, FileType fileType) {
        this.path = path;
        this.originName = originName;
        this.fileType = fileType;
    }
}
