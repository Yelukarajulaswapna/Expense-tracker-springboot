package com.ex.expensetracker.Controller;

import com.ex.expensetracker.Service.UserService;
import com.ex.expensetracker.Security.JwtUtil;
import com.ex.expensetracker.model.AppUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        AppUser user = userService.register(
                request.getUsername(),
                request.getPassword()
        );
        return ResponseEntity.ok(user);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );

        String token = jwtUtil.generateToken(req.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }

    // DTOs
    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}