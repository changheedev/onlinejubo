package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.Jubo;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboRepository;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRegistrationPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
    public JuboDetails register(JuboRegistrationPayload payload) {

        Church church = churchRepository.findById(payload.getChurchId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("ID [%s]로 교회 정보를 찾을 수 없습니다.", payload.getChurchId())
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
}
