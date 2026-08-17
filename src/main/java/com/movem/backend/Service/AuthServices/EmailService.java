package com.movem.backend.Service.AuthServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Movem Login Code");
        message.setText("Your one-time login code is: " + otpCode +
                "\n\nThis code expires in 5 minutes. If you didn't request this, please ignore this email.");
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Movem Password Reset Code");
        message.setText("Your password reset code is: " + otpCode +
                "\n\nThis code expires in 5 minutes. If you didn't request a password reset, please ignore this email — your password won't be changed unless this code is used.");
        mailSender.send(message);
    }

    public void sendEmailVerification(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify Your Movem Email");
        message.setText("Your verification code is: " + code +
                "\n\nEnter this code in the app to activate your account. This code expires in 15 minutes.");
        mailSender.send(message);
    }
}