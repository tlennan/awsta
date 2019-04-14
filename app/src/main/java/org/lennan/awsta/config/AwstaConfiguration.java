package org.lennan.awsta.config;

import org.lennan.awsta.Awsta;
import org.lennan.awsta.repositories.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwstaConfiguration {
	@Autowired
	AssetRepository repository;

	@Bean
	public Awsta getAwsta() {
		return new Awsta(repository);
	}
}
