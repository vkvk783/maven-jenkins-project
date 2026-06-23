package com.weather.monitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private List<String> triggeredAlerts = new ArrayList<>();

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendAlertEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

        //adding alert message to list for visualization
        triggeredAlerts.add(text);
    }

    public List<String> getTriggeredAlerts() {
        return triggeredAlerts; // Return the list of triggered alerts
    }
}

