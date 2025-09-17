package com.alphano.alphano.security.service;

import com.alphano.alphano.domain.user.dao.UserRepository;
import com.alphano.alphano.domain.user.domain.User;
import com.alphano.alphano.security.exception.IdentifierNotFoundException;
import com.alphano.alphano.security.principal.PrincipalUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByIdentifier(username)
                .orElseThrow(() -> IdentifierNotFoundException.EXCEPTION);
        return new PrincipalUserDetails(user);
    }

    public UserDetails loadUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> IdentifierNotFoundException.EXCEPTION);
        return new PrincipalUserDetails(user);
    }
}
