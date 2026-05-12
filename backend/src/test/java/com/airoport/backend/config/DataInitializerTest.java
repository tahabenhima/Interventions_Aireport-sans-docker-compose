package com.airoport.backend.config;

import com.airoport.backend.model.Aeroport;
import com.airoport.backend.model.Technicien;
import com.airoport.backend.repository.AeroportRepository;
import com.airoport.backend.repository.TechnicienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataInitializerTest {

    @Mock
    private AeroportRepository aeroportRepository;

    @Mock
    private TechnicienRepository technicienRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_createsDefaultAirportAndDefaultTechnicienWhenMissing() throws Exception {
        Aeroport savedAeroport = new Aeroport("Aéroport principal");
        when(aeroportRepository.findAll()).thenReturn(List.of());
        when(aeroportRepository.save(any(Aeroport.class))).thenReturn(savedAeroport);
        when(technicienRepository.existsByPseudoname("taha")).thenReturn(false);

        dataInitializer.run();

        verify(aeroportRepository).save(any(Aeroport.class));
        ArgumentCaptor<Technicien> technicienCaptor = ArgumentCaptor.forClass(Technicien.class);
        verify(technicienRepository).save(technicienCaptor.capture());

        Technicien created = technicienCaptor.getValue();
        assertEquals("Taha", created.getFirstname());
        assertEquals("Admin", created.getLastname());
        assertEquals("taha", created.getPseudoname());
        assertEquals("admin", created.getRole());
        assertEquals("taha", created.getMotDePass());
        assertEquals(savedAeroport, created.getAeroport());
    }

    @Test
    void run_reusesExistingAirportAndSkipsTechnicienCreationWhenTahaExists() throws Exception {
        Aeroport existingAeroport = new Aeroport("CMN");
        when(aeroportRepository.findAll()).thenReturn(List.of(existingAeroport));
        when(technicienRepository.existsByPseudoname("taha")).thenReturn(true);

        dataInitializer.run();

        verify(aeroportRepository, never()).save(any(Aeroport.class));
        verify(technicienRepository, never()).save(any(Technicien.class));
    }
}
