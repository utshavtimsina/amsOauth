package com.aeon.ams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.util.Properties;

@SpringBootApplication
@EnableAuthorizationServer
public class AmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmsApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return   PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public MailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("smsnepal2020@gmail.com");
		mailSender.setPassword("zezlsbxumclryvbe");
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		return mailSender;
	}
	@Bean
	public PasswordEncoder getPasswordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
