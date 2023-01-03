package dev.henriqueluiz.peoplemanager;

import dev.henriqueluiz.peoplemanager.security.RSAKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { RSAKeys.class })
public class PeopleManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeopleManagerApplication.class, args);
	}

}
