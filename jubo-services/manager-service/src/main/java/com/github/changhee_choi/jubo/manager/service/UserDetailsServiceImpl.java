package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.user.ChurchManager;
import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.manager.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public ChurchManagerDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("해당 이메일로 가입된 회원 정보를 찾을 수 없습니다.");
        }

        User user = optionalUser.get();

        if (!(user instanceof ChurchManager)) {
            throw new UsernameNotFoundException("ChurchManager 타입의 회원이 아닙니다.");
        }

        return new ChurchManagerDetails((ChurchManager) user);
    }
}
