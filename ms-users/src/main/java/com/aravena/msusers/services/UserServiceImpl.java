package com.aravena.msusers.services;

import com.aravena.msusers.dto.UserDTO;
import com.aravena.msusers.entities.LoginUser;
import com.aravena.msusers.entities.User;
import com.aravena.msusers.handlers.NotFoundException;
import com.aravena.msusers.handlers.UnauthorizedException;
import com.aravena.msusers.handlers.UserBlockedException;
import com.aravena.msusers.repositories.LoginUserRepository;
import com.aravena.msusers.repositories.RolRepository;
import com.aravena.msusers.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    public static final int FIRST_ATTEMPT = 1;
    public static final int LAST_ATTEMPT = 5;
    public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final LoginUserRepository loginUserRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository, LoginUserRepository loginUserRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.loginUserRepository = loginUserRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Optional<UserDTO> getUserByName(String email) {
        return userRepository.findByEmail(email).map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .map(u -> modelMapper.map(u, User.class))
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", email)));

        if(!user.isStatus() && !user.isTemporal()){
            throw new UserBlockedException(String.format("user %s is blocked", email));
        }

        if(!email.equals(user.getEmail()) || !password.equals(user.getPassword())){
            validateLoginUser(user);
        }

        return true;
    }

    @Override
    public boolean sendEmailUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .map(u -> modelMapper.map(u, User.class))
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", email)));

        user.setPassword(password);
        user.setTemporal(true);
        user.setTemporalDate(LocalDateTime.now());
        userRepository.save(user);

        return true;
    }

    private void validateLoginUser(User user){
        LoginUser loginUser = loginUserRepository.findById(user.getId())
                .map(lu -> modelMapper.map(lu, LoginUser.class))
                .orElseGet(() -> {
                    createLoginUser(user);
                    return null;
                });

        if(loginUser != null){
            updateBlockedUser(loginUser, user);
        }
        throw new UnauthorizedException(INVALID_USERNAME_OR_PASSWORD);
    }
    private void createLoginUser(User user) {
        LoginUser loginUserIns = new LoginUser();
        loginUserIns.setIdUser(user.getId());
        loginUserIns.setUsername(user.getEmail());
        loginUserIns.setAttempts(FIRST_ATTEMPT);
        loginUserIns.setBlocked(false);
        loginUserIns.setDateAttempt(LocalDateTime.now());
        loginUserRepository.save(loginUserIns);
    }
    private void updateBlockedUser(LoginUser loginUser, User user) {
        if(loginUser.getAttempts() >= LAST_ATTEMPT){

            user.setStatus(false);
            user.setPassword("");
            userRepository.save(user);

            loginUser.setBlocked(true);
            loginUser.setDateAttempt(LocalDateTime.now());
            loginUserRepository.save(loginUser);

            throw new UserBlockedException(String.format("user %s is blocked", user.getEmail()));
        } else {
            loginUser.setAttempts(loginUser.getAttempts()+1);
            loginUser.setDateAttempt(LocalDateTime.now());
            loginUserRepository.save(loginUser);
        }
    }

    @Override
    public User unlockUser(String email, String newPassword) {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new NotFoundException(String.format("user %s not found", email));
        }

        if(!user.get().getEmail().equals(email) || !user.get().getPassword().equals(newPassword)){
            throw new UnauthorizedException(INVALID_USERNAME_OR_PASSWORD);
        }

        if(!user.get().isStatus()){
            user.get().setStatus(true);
            user.get().setPassword(newPassword);
            user.get().setTemporal(false);
            user.get().setTemporalDate(LocalDateTime.now());
            userRepository.save(user.get());
            loginUserRepository.deleteById(user.get().getId());
        }

        return user.get();
    }
}
