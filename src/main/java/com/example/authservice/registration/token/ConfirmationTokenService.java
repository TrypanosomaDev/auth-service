package com.example.authservice.registration.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);
    }
    public ConfirmationToken getToken(String token){

        return confirmationTokenRepository
                .findByToken(token).orElseThrow(() -> new IllegalStateException("token not found"));
    }

    public void setConfirmedAt(String token, LocalDateTime confirmedAt){
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(token).orElseThrow(() -> new IllegalStateException("token not found"));
        confirmationToken.setConfirmedAt(confirmedAt);
        confirmationTokenRepository.save(confirmationToken);
    }

    public void deleteConfirmationToken(Long id){
        confirmationTokenRepository.deleteById(id);
    }

}
