package com.example.demo.socket;

import com.example.demo.model.CapacidadPlanta;
import com.example.demo.service.CapacidadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

@Component
public class SocketServer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    @Value("${socket.server.port:9090}")
    private int port;

    private final CapacidadService capacidadService;

    public SocketServer(CapacidadService capacidadService) {
        this.capacidadService = capacidadService;
    }

    @Override
    public void run(String... args) {
        new Thread(() -> startServer()).start();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("ContSocket server started on port {}", port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Client connected: {}", clientSocket.getInetAddress());
                    handleClient(clientSocket);
                } catch (Exception e) {
                    logger.error("Error handling client: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error starting socket server: {}", e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                logger.info("Received command: {}", inputLine);
                String response = processCommand(inputLine);
                out.println(response);
                logger.info("Sent response: {}", response);
            }
        } catch (Exception e) {
            logger.error("Error in client communication: {}", e.getMessage());
        }
    }

    private String processCommand(String command) {
        try {
            if (command.equals("PING")) {
                return "PONG";
            } else if (command.startsWith("CAPACITY:")) {
                String[] parts = command.split(":");
                LocalDate fecha = parts.length > 1 ? LocalDate.parse(parts[1]) : LocalDate.now();
                CapacidadPlanta cap = capacidadService.getCapacidad(fecha);
                return String.format("CAPACITY:%.2f:%.2f:%.2f",
                        cap.getCapacidadTotal(),
                        cap.getCapacidadDisponible(),
                        cap.getOcupacionActual());
            } else if (command.startsWith("ASSIGN:")) {
                String[] parts = command.split(":");
                if (parts.length >= 4) {
                    // Format: ASSIGN:ID:WEIGHT:DATE
                    String id = parts[1];
                    float peso = Float.parseFloat(parts[2].replace(",", ".")); // Handle potential locale issues
                    LocalDate fecha = LocalDate.parse(parts[3]);

                    capacidadService.actualizarCapacidad(fecha, peso);
                    logger.info("Assignment processed: ID={}, Weight={}, Date={}", id, peso, fecha);
                    return "OK";
                }
                return "ERROR:Invalid format";
            } else {
                return "ERROR:Unknown command";
            }
        } catch (Exception e) {
            return "ERROR:" + e.getMessage();
        }
    }
}
