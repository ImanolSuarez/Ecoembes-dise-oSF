package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;


@Service
public class SessionStateManager {

    private final Map<String, SessionState> activeSessions = new ConcurrentHashMap<>();
    
    private final Map<String, String> emailToToken = new ConcurrentHashMap<>();

    public static class SessionState {
        private final String token;
        private final String email;
        private final LocalDateTime loginTime;
        private LocalDateTime lastActivity;
        private final List<OperationLog> operationHistory;
        private boolean active;

        public SessionState(String token, String email) {
            this.token = token;
            this.email = email;
            this.loginTime = LocalDateTime.now();
            this.lastActivity = LocalDateTime.now();
            this.operationHistory = new ArrayList<>();
            this.active = true;
        }

        public String getToken() {
            return token;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getLoginTime() {
            return loginTime;
        }

        public LocalDateTime getLastActivity() {
            return lastActivity;
        }

        public void updateActivity() {
            this.lastActivity = LocalDateTime.now();
        }

        public List<OperationLog> getOperationHistory() {
            return new ArrayList<>(operationHistory);
        }

        public void addOperation(String operationType, String description) {
            operationHistory.add(new OperationLog(operationType, description));
            updateActivity();
        }

        public boolean isActive() {
            return active;
        }

        public void deactivate() {
            this.active = false;
        }
    }

    public static class OperationLog {
        private final LocalDateTime timestamp;
        private final String operationType;
        private final String description;

        public OperationLog(String operationType, String description) {
            this.timestamp = LocalDateTime.now();
            this.operationType = operationType;
            this.description = description;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getOperationType() {
            return operationType;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s: %s", timestamp, operationType, description);
        }
    }

 
    public void createSession(String token, String email) {
        removeSessionByEmail(email);
        
        SessionState session = new SessionState(token, email);
        activeSessions.put(token, session);
        emailToToken.put(email, token);
        
        session.addOperation("LOGIN", "Usuario inició sesión");
    }

    
    public boolean isSessionActive(String token) {
        SessionState session = activeSessions.get(token);
        return session != null && session.isActive();
    }

   
    public Optional<String> getEmailByToken(String token) {
        SessionState session = activeSessions.get(token);
        return session != null ? Optional.of(session.getEmail()) : Optional.empty();
    }

   
    public void logOperation(String email, String operationType, String description) {
        String token = emailToToken.get(email);
        if (token != null) {
            SessionState session = activeSessions.get(token);
            if (session != null) {
                session.addOperation(operationType, description);
            }
        }
    }

    public List<String> getOperationHistory(String email) {
        String token = emailToToken.get(email);
        if (token != null) {
            SessionState session = activeSessions.get(token);
            if (session != null) {
                return session.getOperationHistory().stream()
                        .map(OperationLog::toString)
                        .toList();
            }
        }
        return new ArrayList<>();
    }

   
    public Optional<String> getSessionInfo(String token) {
        SessionState session = activeSessions.get(token);
        if (session != null) {
            return Optional.of(String.format(
                "Session Info - Email: %s, Login: %s, Last Activity: %s, Operations: %d, Active: %s",
                session.getEmail(),
                session.getLoginTime(),
                session.getLastActivity(),
                session.getOperationHistory().size(),
                session.isActive()
            ));
        }
        return Optional.empty();
    }

 
    public void removeSession(String token) {
        SessionState session = activeSessions.remove(token);
        if (session != null) {
            session.deactivate();
            emailToToken.remove(session.getEmail());
        }
    }

    public void removeSessionByEmail(String email) {
        String token = emailToToken.remove(email);
        if (token != null) {
            SessionState session = activeSessions.remove(token);
            if (session != null) {
                session.deactivate();
            }
        }
    }

   
    public void updateActivity(String token) {
        SessionState session = activeSessions.get(token);
        if (session != null) {
            session.updateActivity();
        }
    }

 
    public int getActiveSessionCount() {
        return (int) activeSessions.values().stream()
                .filter(SessionState::isActive)
                .count();
    }

   
    public void cleanupInactiveSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        List<String> tokensToRemove = new ArrayList<>();
        
        activeSessions.forEach((token, session) -> {
            if (session.getLastActivity().isBefore(cutoffTime)) {
                tokensToRemove.add(token);
            }
        });
        
        tokensToRemove.forEach(this::removeSession);
    }

   
    public List<String> getAllActiveSessions() {
        return activeSessions.values().stream()
                .filter(SessionState::isActive)
                .map(session -> String.format("Email: %s, Login: %s, Operations: %d",
                        session.getEmail(),
                        session.getLoginTime(),
                        session.getOperationHistory().size()))
                .toList();
    }
}