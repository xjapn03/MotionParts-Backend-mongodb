package com.motionparts.ecommerce.services;

import com.motionparts.ecommerce.dto.LoginRequest;
import com.motionparts.ecommerce.dto.RegisterRequest;
import com.motionparts.ecommerce.models.User;
import com.motionparts.ecommerce.repositories.UserRepository;
import com.motionparts.ecommerce.mappers.UserMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Key signingKey;
    private final long jwtExpiration;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.jwt.secret}") String jwtSecret,
        @Value("${app.jwt.expiration}") long jwtExpiration
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpiration = jwtExpiration;
    }

    public String authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("El usuario no existe"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("La contraseña es incorrecta");
        }

        return generateToken(user);
    }

    public User register(RegisterRequest request) {
        // Mapear los DTOs a las entidades correspondientes
        User user = UserMapper.toUser(request.getUser(), request.getUserInfo(), request.getUser().getPassword());

        // Llamar al servicio para crear el usuario
        return userService.createUser(user);  // Pasamos solo el objeto User
    }

    private String generateToken(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(User.Role::getName)  // Clase anidada
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));
    }
}
