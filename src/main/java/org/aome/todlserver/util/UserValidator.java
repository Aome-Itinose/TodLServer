package org.aome.todlserver.util;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.User;
import org.aome.todlserver.services.UsersService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UsersService usersService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if(usersService.userExist(user.getUsername())){
            errors.rejectValue("username", "", String.format("User '%s' already exist", user.getUsername()));
        }
    }
}
