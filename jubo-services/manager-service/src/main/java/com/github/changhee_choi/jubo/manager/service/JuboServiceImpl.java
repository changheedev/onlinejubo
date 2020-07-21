package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.Jubo;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboRepository;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@Service
@Transactional
@RequiredArgsConstructor
public class JuboServiceImpl implements JuboService {

    private final JuboRepository juboRepository;
    private final ChurchRepository churchRepository;

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

        return JuboDetails.of(jubo);
    }

    @Override
    public JuboDetails update(UUID churchId, Long juboId, JuboRequest payload) {
        Jubo jubo = juboRepository.findById(juboId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("ID [%s]로 주보 정보를 찾을 수 없습니다.", juboId)
                        ));

        if (!jubo.getChurch().getId().equals(churchId)) {
            throw new AccessDeniedException("해당 교회에 대한 권한이 없습니다.");
        }

        jubo.updateTitle(payload.getTitle());
        jubo.updateStartDate(payload.getStartDate());
        juboRepository.save(jubo);
        return JuboDetails.of(jubo);
    }
}
