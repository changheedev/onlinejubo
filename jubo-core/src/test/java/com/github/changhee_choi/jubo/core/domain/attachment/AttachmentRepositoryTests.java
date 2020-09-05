package com.github.changhee_choi.jubo.core.domain.attachment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Changhee Choi
 * @since 22/06/2020
 */
@DataJpaTest
class AttachmentRepositoryTests {

    @Autowired
    private AttachmentRepository attachmentRepository;

    private Attachment createAttachmentEntity() {
        Attachment attachment = Attachment.builder()
                .path("/test/upload/path/" + UUID.randomUUID().toString().replace("-", "") + ".jpg")
                .originName("testFile.jpg")
                .fileType(FileType.IMAGE)
                .build();

        return attachmentRepository.save(attachment);
    }

    @Test
    void create() {
        Attachment attachment = createAttachmentEntity();
        assertThat(attachment.getId().toString().length()).isEqualTo(36);
    }

    @Test
    void read() {
        Attachment attachment = createAttachmentEntity();

        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachment.getId());
        assertThat(optionalAttachment.isPresent()).isTrue();

        System.out.println(optionalAttachment.get().getPath());
    }
}