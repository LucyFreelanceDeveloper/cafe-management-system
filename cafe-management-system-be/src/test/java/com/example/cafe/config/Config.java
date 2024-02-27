package com.example.cafe.config;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class Config {

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        return javaMailSender;
    }
}