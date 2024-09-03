package com.uade.tpo.MarketPlace.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//
import com.uade.tpo.MarketPlace.controllers.auth.AuthenticationRequest;
import com.uade.tpo.MarketPlace.controllers.auth.AuthenticationResponse;
import com.uade.tpo.MarketPlace.controllers.auth.RegisterRequest;
import com.uade.tpo.MarketPlace.controllers.config.JwtService;
import com.uade.tpo.MarketPlace.entity.NivelVendedor;
import com.uade.tpo.MarketPlace.entity.Usuario;
import com.uade.tpo.MarketPlace.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UsuarioRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var user = Usuario.builder()
                                .Nombre(request.getNombre())
                                .Apellido(request.getApellido())
                                .mail(request.getMail())
                                .Contraseña(passwordEncoder.encode(request.getContraseña()))
                                .role(request.getRole())
                                .NombreUsuario(request.getNombreUsuario())
                                .nivel(NivelVendedor.Bajo)
                                .build();

                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getMail(),
                                                request.getContraseña()));

                var user = repository.findByMail(request.getMail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }
}
