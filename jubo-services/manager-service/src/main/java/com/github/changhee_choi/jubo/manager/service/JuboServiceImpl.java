package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentNotFoundException;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchNotFoundException;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.*;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentPayload;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRequestPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@Service
@Transactional
@RequiredArgsConstructor
public class JuboServiceImpl implements JuboService {

    private final JuboRepository juboRepository;
    private final JuboContentRepository juboContentRepository;
    private final ChurchRepository churchRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public JuboDetails register(JuboRequestPayload payload) {

        Church church = churchRepository.findById(payload.getChurchId())
                .orElseThrow(() -> new ChurchNotFoundException("교회 정보를 찾을 수 없습니다. [" + payload.getChurchId() + "]"));

        Jubo jubo = Jubo.builder()
                .title(payload.getTitle())
                .startDate(payload.getStartDate())
                .build();

        jubo.setChurch(church);
        juboRepository.save(jubo);

        for (JuboContentPayload contentPayload : payload.getJuboContents()) {
            JuboContent juboContent = JuboContent.builder()
                    .title(contentPayload.getTitle())
                    .content(contentPayload.getContent())
                    .build();

            if (contentPayload.getAttachmentIds().size() > 0) {
                List<Attachment> attachments = attachmentRepository.findAllById(contentPayload.getAttachmentIds());
                if(attachments.size() != contentPayload.getAttachmentIds().size()) {
                    throw new AttachmentNotFoundException("주보 등록에 요청된 첨부 파일의 일부를 찾을 수 없습니다.");
                }
                juboContent.addAttachments(attachments);
            }

            juboContent.setJubo(jubo);
            juboContentRepository.save(juboContent);
        }

        return JuboDetails.of(jubo);
    }
}
