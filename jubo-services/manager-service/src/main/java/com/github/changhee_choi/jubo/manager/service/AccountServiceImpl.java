package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.user.*;
import com.github.changhee_choi.jubo.core.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.manager.web.payload.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ChurchRepository churchRepository;

    @Override
    public ChurchManagerDetails signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("중복된 이메일로 회원가입이 요청되었습니다.");
        }

        Church church = Church.builder().name(request.getChurchName()).memberNum(request.getChurchMemberNum()).build();
        churchRepository.save(church);

        ChurchManager manager = ChurchManager.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .church(church)
                .build();

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new RoleNotFoundException("같은 이름으로 등록된 권한 정보를 찾을 수 없습니다."));

        manager.addRole(role);
        userRepository.save(manager);

        return new ChurchManagerDetails(manager);
    }
}
