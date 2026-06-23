package com.weather.monitoring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendAlertEmail() {
        // Call the method to send an alert email
        emailService.sendAlertEmail("kulkarnivivek1992@gmail.com", "Test Subject", "Test Alert Message");

        // Verify that the email is sent
        verify(emailSender).send(any(SimpleMailMessage.class));

        // Verify that the alert message is added to the list
        assertEquals(1, emailService.getTriggeredAlerts().size());
        assertEquals("Test Alert Message", emailService.getTriggeredAlerts().get(0));
    }
}
