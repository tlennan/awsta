package org.lennan.awsta.premises;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.lennan.awsta")
public class PremisesApplication {

	private static Log logger = LogFactory.getLog(PremisesApplication.class);

	@Bean(name = "premises")
	Premises getPremises() {
		return new Premises();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(PremisesApplication.class, args);
	}
}