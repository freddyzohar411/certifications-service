package com.avensys.rts.certificationsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CertificationsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertificationsServiceApplication.class, args);
	}

}
