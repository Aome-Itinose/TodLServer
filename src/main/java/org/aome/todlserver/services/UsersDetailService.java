package org.aome.todlserver.services;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.User;
import org.aome.todlserver.repositories.UsersRepository;
import org.aome.todlserver.security.UsersDetails;
import org.aome.todlserver.util.exceptions.user.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsersDetailService implements UserDetailsService {
    private final UsersRepository usersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<User> user = usersRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }
        return new UsersDetails(user.get());
    }
}
