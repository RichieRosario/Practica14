package pucmm.practica14.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


public interface MailService {

    public void sendSimpleMessage( String to, String subject, String text);
}