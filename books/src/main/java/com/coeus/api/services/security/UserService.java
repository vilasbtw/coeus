package com.coeus.api.services.security;

import com.coeus.api.models.dtos.security.UserStatusDTO;
import com.coeus.api.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateUserStatus(Long id, UserStatusDTO dto)  throws UsernameNotFoundException {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        if (dto.getEnabled() != null) user.setEnabled(dto.getEnabled());
        if (dto.getAccountLocked() != null) user.setAccountLocked(dto.getAccountLocked());
        if (dto.getAccountExpired() != null) user.setAccountExpired(dto.getAccountExpired());
        if (dto.getCredentialsExpired() != null) user.setCredentialsExpired(dto.getCredentialsExpired());

        userRepository.save(user);
    }
}