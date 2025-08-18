package com.openschool.system.identity.service;

import com.openschool.domain.identity.model.Account;
import com.openschool.system.identity.exception.InvalidCredentialsException;
import com.openschool.system.identity.port.in.LoginUseCase;
import com.openschool.system.identity.port.out.AccountRepositoryPort;
import com.openschool.system.identity.port.out.PasswordEncoderPort;
import com.openschool.system.identity.port.out.TokenGeneratorPort;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class LoginService implements LoginUseCase {
    private AccountRepositoryPort accountRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenGeneratorPort tokenGeneratorPort;

    @Override
    public String login(String username, String rawPassword) {
        Optional<Account> user = accountRepositoryPort.findByUsername(username);
        if (user.isEmpty() || !passwordEncoderPort.matches(rawPassword, user.get().getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return tokenGeneratorPort.generateToken(user.get());
    }
}
