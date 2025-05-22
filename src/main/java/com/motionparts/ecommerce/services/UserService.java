package com.motionparts.ecommerce.services;

import com.motionparts.ecommerce.dto.UserDTO;
import com.motionparts.ecommerce.dto.UserInfoDTO;
import com.motionparts.ecommerce.mappers.UserMapper;
import com.motionparts.ecommerce.models.User;
import com.motionparts.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        // Validación de usuario único basado en username
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Validación de correo único
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Codificar la contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        user.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

        // Guardar el nuevo usuario en la base de datos
        return userRepository.save(user);
    }

    public User updateUser(String username, UserDTO userDTO, UserInfoDTO userInfoDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        String encodedPassword = userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
            ? passwordEncoder.encode(userDTO.getPassword())
            : null;

        UserMapper.updateUserFromDTO(user, userDTO, userInfoDTO, encodedPassword);
        user.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

        return userRepository.save(user);
    }
}