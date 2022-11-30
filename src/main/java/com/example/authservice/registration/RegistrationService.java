package com.example.authservice.registration;

import com.example.authservice.appuser.AppUser;
import com.example.authservice.appuser.AppUserRole;
import com.example.authservice.appuser.AppUserService;
import com.example.authservice.config.RabbitConfig;
import com.example.authservice.registration.event.RegistrationEventModel;
import com.example.authservice.registration.token.ConfirmationToken;
import com.example.authservice.registration.token.ConfirmationTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public String register(RegistrationRequest request){

        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getUserRole()
                )
        );
    }

    @Transactional
    public String confirmToken(String token) throws JsonProcessingException {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if(expiresAt.isBefore(now)){
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token, now);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        if(confirmationToken.getAppUser().getAppUserRole() == AppUserRole.USER){
            String registrationEventString = objectMapper.
                    writeValueAsString(new RegistrationEventModel(confirmationToken.getAppUser()));
            rabbitTemplate.convertAndSend(RabbitConfig.REGISTRATION_EVENT, registrationEventString);
        }

        return "confirmed";
    }
}
