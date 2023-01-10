package com.scb.externo.config;

import java.time.Duration;
import java.util.Properties;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:env/mail.properties")
public class EmailConfig {

    @Bean
    JavaMailSender mailSender(Environment env) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(env.getProperty("mail.smtp.host"));
        mailSender.setPort(env.getProperty("mail.smtp.port", Integer.class));
        mailSender.setUsername(env.getProperty("mail.smtp.username"));
        mailSender.setPassword(env.getProperty("mail.smtp.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(3000))
		.setReadTimeout(Duration.ofMillis(3000))
		.build();
    }

}
