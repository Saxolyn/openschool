package com.openschool.system.identity.port.in;

public interface InitRootUserUseCase {
    void initRoot(String username, String rawPassword);
}