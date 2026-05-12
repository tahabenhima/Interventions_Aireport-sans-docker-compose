package com.airoport.backend.service;

import com.airoport.backend.dto.LoginRequest;
import com.airoport.backend.dto.LoginResponse;
import com.airoport.backend.model.Aeroport;
import com.airoport.backend.model.Technicien;
import com.airoport.backend.repository.TechnicienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private TechnicienRepository technicienRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldAuthenticateHardcodedDemoUserWithoutDatabase() {
        LoginRequest req = new LoginRequest();
        req.setPseudoname("taha");
        req.setMotDePass("taha");

        LoginResponse response = authService.login(req);

        assertEquals(-1, response.id);
        assertEquals("admin", response.role);
        assertNotNull(response.token);
        verifyNoInteractions(technicienRepository);
    }

    @Test
    void login_ShouldKeepExistingDatabaseAuthenticationForOtherUsers() {
        LoginRequest req = new LoginRequest();
        req.setPseudoname("ali");
        req.setMotDePass("pass123");

        Technicien technicien = new Technicien("Ali", "Ben", "ali", "technicien", "pass123", new Aeroport("A1"));
        when(technicienRepository.findByPseudoname("ali")).thenReturn(Optional.of(technicien));

        LoginResponse response = authService.login(req);

        assertEquals("technicien", response.role);
        assertEquals("Ali", response.firstname);
        assertNotNull(response.token);
        verify(technicienRepository).findByPseudoname("ali");
    }
}
