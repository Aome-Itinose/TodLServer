package org.aome.todlserver.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.User;
import org.aome.todlserver.repositories.UsersRepository;
import org.aome.todlserver.util.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username){
        return usersRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public boolean userExist(String username){
        return usersRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public void createNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(enrich(user));
    }

    @Transactional
    public void setAdmin(User user) throws UserNotFoundException{
        usersRepository.findByUsername(user.getUsername()).orElseThrow(UserNotFoundException::new).setRole("ROLE_ADMIN");
    }

    private User enrich(User user){
        user.setRole("ROLE_USER");

        return user;
    }
}