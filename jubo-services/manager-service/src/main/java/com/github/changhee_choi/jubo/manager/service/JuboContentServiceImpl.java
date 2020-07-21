package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.*;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 17/07/2020
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class JuboContentServiceImpl implements JuboContentService {

    private final JuboRepository juboRepository;
    private final JuboContentRepository juboContentRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public JuboContentDetails register(UUID churchIdOfManager, Long juboId, JuboContentRequest payload) {

        Jubo jubo = juboRepository.findById(juboId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("ID [%s] 로 주보 데이터를 찾을 수 없습니다.", juboId)));

        if (!churchIdOfManager.equals(jubo.getChurch().getId())) {
            throw new AccessDeniedException("해당 교회에 대한 권한이 없습니다.");
        }

        JuboContent juboContent = JuboContent.builder()
                .title(payload.getTitle())
                .content(payload.getContent())
                .build();

        if (payload.getAttachmentIds().size() > 0) {
            List<Attachment> attachments = attachmentRepository.findAllById(payload.getAttachmentIds());
            if (attachments.size() != payload.getAttachmentIds().size()) {
                throw new EntityNotFoundException("등록되지 않은 첨부 파일의 ID가 사용되었습니다.");
            }
            juboContent.addAttachments(attachments);
        }

        juboContent.setJubo(jubo);
        juboContentRepository.save(juboContent);

        return JuboContentDetails.of(juboContent);
    }
}
