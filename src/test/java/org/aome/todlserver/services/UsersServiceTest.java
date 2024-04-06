package org.aome.todlserver.services;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.User;
import org.aome.todlserver.repositories.UsersRepository;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class UsersServiceTest {
    private final UsersRepository usersRepository;

    @Test
    void findByUsername() {
        User user = usersRepository.findByUsername("admin").get();
        
    }
}