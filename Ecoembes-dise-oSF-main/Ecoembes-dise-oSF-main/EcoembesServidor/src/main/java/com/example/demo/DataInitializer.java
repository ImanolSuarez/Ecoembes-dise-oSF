package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Contenedor;
import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.service.ContenedorService;
import com.example.demo.service.LoginService;
import com.example.demo.service.PlantaReciclajeService;

@Component
public class DataInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	private final LoginService loginService;
	private final ContenedorService contenedorService;
	private final PlantaReciclajeService plantaService;

	public DataInitializer(LoginService loginService, ContenedorService contenedorService,
			PlantaReciclajeService plantaService) {
		this.loginService = loginService;
		this.contenedorService = contenedorService;
		this.plantaService = plantaService;
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Iniciando la carga de datos de prueba...");

		// Crear usuario administrador para pruebas del cliente
		String email = "admin@ecoembes.es";
		String password = "123";

		loginService.register(email, password);

		logger.info("Usuario de prueba creado: {} / {}", email, password);

		// Crear contenedores de prueba
		Contenedor c1 = new Contenedor("C-001", "Calle Gran Vía, Bilbao", "48001", 1000.0f, null);
		Contenedor c2 = new Contenedor("C-002", "Plaza Moyúa, Bilbao", "48009", 800.0f, null);
		Contenedor c3 = new Contenedor("C-003", "Avenida de la Libertad, San Sebastián", "20004", 1200.0f, null);

		try {
			contenedorService.create(c1);
			contenedorService.create(c2);
			contenedorService.create(c3);
			logger.info("Contenedores de prueba creados en Bilbao y San Sebastián.");
		} catch (Exception e) {
			logger.warn("No se pudieron crear los contenedores (quizás ya existen): {}", e.getMessage());
		}

		// Crear planta de reciclaje de prueba
		PlantaReciclaje p1 = new PlantaReciclaje("BilboRecicla", "Zorrozaurre, Bilbao", 50.0f);
		try {
			plantaService.create(p1);
			logger.info("Planta de reciclaje de prueba creada: BilboRecicla");

			// Asignar el contenedor C-001 a la planta
			c1.setPlantaAsignada(p1);
			contenedorService.update(c1.getId(), c1);
			logger.info("Contenedor C-001 asignado a la planta BilboRecicla");
		} catch (Exception e) {
			logger.warn("No se pudo crear la planta o asignar el contenedor: {}", e.getMessage());
		}

		logger.info("Carga de datos completada.");
	}
}
