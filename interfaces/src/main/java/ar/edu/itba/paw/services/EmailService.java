package ar.edu.itba.paw.services;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, String template, Map<String,Object> variables);
}
