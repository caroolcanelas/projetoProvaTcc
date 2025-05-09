package com.projetoProvaTcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableJpaRepositories(basePackages = "com.projetoProvaTcc.repository")
@EntityScan(basePackages = "com.projetoProvaTcc.model")
public class PrjProvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrjProvaApplication.class, args);
	}
}
