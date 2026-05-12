package com.airoport.backend.config;

import com.airoport.backend.model.Aeroport;
import com.airoport.backend.model.Technicien;
import com.airoport.backend.repository.AeroportRepository;
import com.airoport.backend.repository.TechnicienRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final String DEFAULT_PSEUDONAME = "taha";
    private static final String DEFAULT_PASSWORD = "taha";

    private final AeroportRepository aeroportRepository;
    private final TechnicienRepository technicienRepository;

    public DataInitializer(AeroportRepository aeroportRepository, TechnicienRepository technicienRepository) {
        this.aeroportRepository = aeroportRepository;
        this.technicienRepository = technicienRepository;
    }

    @Override
    public void run(String... args) {
        Aeroport aeroport = aeroportRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> aeroportRepository.save(new Aeroport("Aéroport principal")));

        if (!technicienRepository.existsByPseudoname(DEFAULT_PSEUDONAME)) {
            Technicien technicien = new Technicien(
                    "Taha",
                    "Admin",
                    DEFAULT_PSEUDONAME,
                    "admin",
                    DEFAULT_PASSWORD,
                    aeroport
            );
            technicienRepository.save(technicien);
        }
    }
}
