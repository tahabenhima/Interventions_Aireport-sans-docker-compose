package com.airoport.backend.config;

import com.airoport.backend.model.Aeroport;
import com.airoport.backend.model.Technicien;
import com.airoport.backend.repository.AeroportRepository;
import com.airoport.backend.repository.TechnicienRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AeroportRepository aeroportRepository;
    private final TechnicienRepository technicienRepository;
    private final String defaultPseudoname;
    private final String defaultPassword;

    public DataInitializer(
            AeroportRepository aeroportRepository,
            TechnicienRepository technicienRepository,
            @Value("${app.seed.default-technicien.pseudoname:taha}") String defaultPseudoname,
            @Value("${app.seed.default-technicien.password:taha}") String defaultPassword) {
        this.aeroportRepository = aeroportRepository;
        this.technicienRepository = technicienRepository;
        this.defaultPseudoname = defaultPseudoname;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public void run(String... args) {
        Aeroport aeroport = aeroportRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> aeroportRepository.save(new Aeroport("Aéroport principal")));

        if (!technicienRepository.existsByPseudoname(defaultPseudoname)) {
            Technicien technicien = new Technicien(
                    "Taha",
                    "Admin",
                    defaultPseudoname,
                    "admin",
                    defaultPassword,
                    aeroport
            );
            technicienRepository.save(technicien);
        }
    }
}
