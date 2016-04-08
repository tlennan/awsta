package org.lennan.awsta.premises.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("org.lennan.awsta.repositories")
public class MongoConfiguration {

}
