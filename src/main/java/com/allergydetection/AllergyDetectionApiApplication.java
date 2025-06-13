package com.allergydetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AllergyDetectionApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AllergyDetectionApiApplication.class, args);
        System.out.println("🚀 API de Détection d'Allergies Alimentaires");
        System.out.println("📋 Fonctionnalités disponibles:");
        System.out.println("   • Gestion des utilisateurs et aliments");
        System.out.println("   • Journal alimentaire et suivi des symptômes");
        System.out.println("   • Détection intelligente d'allergies");
        System.out.println("   • Planification hebdomadaire des repas");
        System.out.println("   • Gestion de buffets pour événements");
        System.out.println("   • Recommandations personnalisées");
        System.out.println("🔗 API Documentation: http://localhost:8080/swagger-ui.html");
    }
}