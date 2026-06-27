package io.github.thesmoothrere.boilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Spring Boot Boilerplate application.
 * <p>
 * This is the entry point for the Spring Boot application. It configures
 * auto-configuration, component scanning, and starts the embedded server.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@SpringBootApplication
public class SpringBootBoilerplateApplication {

    /**
     * Main entry point for the application.
     *
     * @param args command line arguments
     */
    static void main(String[] args) {
        SpringApplication.run(SpringBootBoilerplateApplication.class, args);
    }

}
