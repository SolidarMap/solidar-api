package br.com.solidarmap.solidar_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public class SolidarApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("ORACLE_URL", dotenv.get("ORACLE_URL"));
		System.setProperty("ORACLE_USER", dotenv.get("ORACLE_USER"));
		System.setProperty("ORACLE_PASSWORD", dotenv.get("ORACLE_PASSWORD"));
		System.setProperty("ORACLE_DRIVER", dotenv.get("ORACLE_DRIVER"));

		SpringApplication.run(SolidarApiApplication.class, args);
	}

}
