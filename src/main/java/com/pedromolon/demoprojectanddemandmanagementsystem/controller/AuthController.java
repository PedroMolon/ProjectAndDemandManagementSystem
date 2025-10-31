package com.pedromolon.demoprojectanddemandmanagementsystem.controller;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.LoginRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.RegisterRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.LoginResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.RegisterResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.User;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Role;
import com.pedromolon.demoprojectanddemandmanagementsystem.repository.UserRepository;
import com.pedromolon.demoprojectanddemandmanagementsystem.security.TokenConfig;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        if (request.role() != null) {
            user.setRoles(Set.of(request.role()));
        } else {
            user.setRoles(Set.of(Role.ROLE_EMPLOYEE));
        }

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(user.getName(), user.getEmail()));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));
    }

}
