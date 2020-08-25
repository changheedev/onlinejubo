package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.*;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentRequest;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@Service
@Transactional
@RequiredArgsConstructor
public class JuboServiceImpl implements JuboService {

    private final ChurchRepository churchRepository;
    private final JuboRepository juboRepository;
    private final JuboContentRepository juboContentRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public JuboDetails register(UUID churchId, JuboRequest payload) {
        Church church = churchRepository.findById(churchId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("ID [%s]로 교회 정보를 찾을 수 없습니다.", churchId)
                        )
                );

        Jubo jubo = Jubo.builder()
                .title(payload.getTitle())
                .startDate(payload.getStartDate())
                .build();

        jubo.setChurch(church);
        juboRepository.save(jubo);

        payload.getContents().forEach(contentPayload -> registerContent(jubo, contentPayload));

        return JuboDetails.of(jubo);
    }

    private void registerContent(Jubo jubo, JuboContentRequest payload) {
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
    }

    @Override
    public JuboDetails update(UUID churchId, Long juboId, JuboRequest payload) {
        Jubo jubo = findJuboById(juboId);

        if (!jubo.getChurch().getId().equals(churchId)) {
            throw new AccessDeniedException("해당 교회에 대한 권한이 없습니다.");
        }

        jubo.updateTitle(payload.getTitle());
        jubo.updateStartDate(payload.getStartDate());
        juboRepository.save(jubo);
        return JuboDetails.of(jubo);
    }

    @Override
    public void delete(UUID churchId, Long juboId) {
        Jubo jubo = findJuboById(juboId);

        if (!jubo.getChurch().getId().equals(churchId)) {
            throw new AccessDeniedException("해당 교회에 대한 권한이 없습니다.");
        }

        jubo.delete();
        juboRepository.save(jubo);
    }

    private Jubo findJuboById(Long id) {
        return juboRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("ID [%s]로 주보 정보를 찾을 수 없습니다.", id)
                        ));
    }
}
