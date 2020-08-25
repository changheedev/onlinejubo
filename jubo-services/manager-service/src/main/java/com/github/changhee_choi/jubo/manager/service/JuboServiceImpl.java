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

    /**
     * 서버에 저장 되기 전 까지는 동작을 취소 할 수 있어야 한다.
     * update 이후에도 컨텐츠들의 순서를 보장해야 한다.
     *
     * 사용자 화면에서 컨텐츠를 새로 등록하거나 수정/삭제 했을 때 개별적으로 DB에 반영하게 되면 사용자가 동작을 취소 했을 때의 처리가 어렵고
     * 순서를 유지하기 위한 필드를 추가해야 하며 매번 순서 정보를 업데이트 해주어야 한다.
     * 따라서, 사용자 화면 단에서는 변경된 이후의 전체 데이터를 서버로 전송하고
     * 서버에서는 기존 컨텐츠 정보를 모두 삭제한 다음 변경된 이후의 컨텐츠 데이터를 새로 등록하는 방식으로 구현한다.
     */
    @Override
    public JuboDetails update(UUID churchId, Long juboId, JuboRequest payload) {
        Jubo jubo = findJuboById(juboId);

        if (!jubo.getChurch().getId().equals(churchId)) {
            throw new AccessDeniedException("해당 교회에 대한 권한이 없습니다.");
        }

        jubo.updateTitle(payload.getTitle());
        jubo.updateStartDate(payload.getStartDate());

        juboContentRepository.deleteAllByJuboId(juboId);
        jubo.getContents().clear();
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
