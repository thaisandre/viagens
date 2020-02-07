package br.com.caelum.viagens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication
public class ViagensApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViagensApplication.class, args);
	}

}
