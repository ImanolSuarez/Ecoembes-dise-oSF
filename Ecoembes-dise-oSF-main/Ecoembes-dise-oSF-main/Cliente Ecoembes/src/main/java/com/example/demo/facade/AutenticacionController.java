package com.example.demo.facade;

import com.example.demo.ServiceProxy.ServiceProxy;

/**
 * Controlador para gestionar el inicio y cierre de sesión.
 */
public class AutenticacionController {

    private final ServiceProxy serviceProxy;

    public AutenticacionController() {
        this.serviceProxy = ServiceProxy.getInstance();
    }

    public String login(String email, String password) throws Exception {
        return serviceProxy.login(email, password);
    }

    public void logout(String token) throws Exception {
        serviceProxy.logout(token);
    }
}
